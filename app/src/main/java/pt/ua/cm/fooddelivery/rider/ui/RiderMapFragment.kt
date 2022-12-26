package pt.ua.cm.fooddelivery.rider.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.SettingsSlicesContract.KEY_LOCATION
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.Update
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.PolyUtil
import org.json.JSONObject
import pt.ua.cm.fooddelivery.BuildConfig.MAPS_API_KEY
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.RiderActivity
import pt.ua.cm.fooddelivery.databinding.FragmentRiderMapBinding
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.network.request.AcceptOrderRequest
import pt.ua.cm.fooddelivery.network.request.UpdateRiderLocationRequest
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber
import java.io.IOException


class RiderMapFragment : Fragment(), OnMapReadyCallback {

    private val args: RiderMapFragmentArgs by navArgs()

    private lateinit var binding: FragmentRiderMapBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var locationPermissionGranted = false
    //private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var cameraPosition: CameraPosition? = null
    private var map: GoogleMap? = null

    private val clientLocation: MutableLiveData<LatLng?> by lazy {
        MutableLiveData<LatLng?>()
    }

    private val lastKnownLocation: MutableLiveData<Location?> by lazy {
        MutableLiveData<Location?>()
    }

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var delivery: DeliveriesResponse? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate with args: $args")

        //lastClientLocation = args.clientAddress
        getLocationFromAddress(requireContext(), args.clientAddress)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity as RiderActivity)

        if (savedInstanceState != null) {
            lastKnownLocation.postValue(savedInstanceState.getParcelable(KEY_LOCATION))
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRiderMapBinding.inflate(layoutInflater)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync(this)

        clientLocation.observe(viewLifecycleOwner) {
            Timber.i("Client Location Observer: $it")
            if(it != null) {
                getDirections()
            }
        }
        lastKnownLocation.observe(viewLifecycleOwner) {
            Timber.i("Last known Location Observer: $it")
            if(it != null) {
                getDirections()
            }

            if (lastKnownLocation.value != null) {
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    LatLng(lastKnownLocation.value!!.latitude,
                        lastKnownLocation.value!!.longitude), DEFAULT_ZOOM.toFloat()))
            }
        }

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(500)
            .setMaxUpdateDelayMillis(1000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.locations[0]

                val updateRiderLocationRequest = UpdateRiderLocationRequest(
                    order_id = args.orderId,
                    rider_lat = location.latitude,
                    rider_lng = location.longitude,
                )

                Api.apiService.updateRiderLocation(updateRiderLocationRequest).enqueue(object :
                    Callback<DeliveriesResponse> {
                    override fun onFailure(call: Call<DeliveriesResponse>, t: Throwable) {
                        Timber.i("error $t")
                    }

                    override fun onResponse(
                        call: Call<DeliveriesResponse>,
                        response: retrofit2.Response<DeliveriesResponse>
                    ) {
                        delivery = response.body()
                    }
                })
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

        binding.deliveryDetailsBtn.setOnClickListener {
            val dialog = BottomSheetDialog(this.requireContext())
            val view=layoutInflater.inflate(R.layout.finish_order,null)

            val imgClose = view.findViewById<ImageView>(R.id.img_close)
            imgClose.setOnClickListener {
                dialog.dismiss()
            }

            val clientName = view.findViewById<TextView>(R.id.client_name)
            val clientAddress = view.findViewById<TextView>(R.id.client_address)
            val finishBtn = view.findViewById<Button>(R.id.finish_delivery_btn)
            val prgbar = view.findViewById<ProgressBar>(R.id.prgbar)

            if(delivery != null) {
                finishBtn.visibility = View.VISIBLE
                prgbar.visibility = View.GONE

                clientName.text = delivery!!.client_name

                clientAddress.text = delivery!!.client_address

                finishBtn.setOnClickListener {
                    Api.apiService.finishDelivery(args.orderId).enqueue(object :
                        Callback<DeliveriesResponse> {
                        override fun onFailure(call: Call<DeliveriesResponse>, t: Throwable) {
                            Timber.i("error $t")
                        }

                        override fun onResponse(
                            call: Call<DeliveriesResponse>,
                            response: retrofit2.Response<DeliveriesResponse>
                        ) {
                            dialog.dismiss()
                            navigateHome()
                        }
                    })
                }
            } else {
                finishBtn.visibility = View.GONE
                prgbar.visibility = View.VISIBLE
            }

            dialog.setContentView(view)
            dialog.show()
        }

        return binding.root
    }

    fun navigateHome() {
        view?.findNavController()
            ?.navigate(R.id.action_riderMapFragment_to_rider_navigation_home)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation.value)
        }
        super.onSaveInstanceState(outState)
    }

    private fun getLocationFromAddress(context: Context, strAddress: String?) {
        val coder = Geocoder(context)

        val strAddress2 = "Universidade de Aveiro, 3810-193 Aveiro"  //TODO
        val address: List<Address>?
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress2!!, 1)
            if (address == null) {
                return
            }
            val location: Address = address[0]
            clientLocation.postValue( LatLng(location.latitude, location.longitude) )
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map

        // Prompt the user for permission.
        getLocationPermission()

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        setClientMarker()
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(activity as RiderActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(activity as RiderActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation.postValue(null)
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Timber.i("Exception: ${e.message}")
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(activity as RiderActivity) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation.postValue(task.result)
                        Timber.i("LAST KNOW LOCATION: ${lastKnownLocation.value}")

                    } else {
                        Timber.i("Current location is null. Using defaults.")
                        Timber.i("Exception: ${task.exception}")
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Timber.i("Exception: ${e.message}")
        }
    }

    private fun setClientMarker() {
        if (clientLocation.value != null) {
            map?.addMarker(MarkerOptions().position(clientLocation.value!!))
        }
    }

    private fun getDirections() {
        Timber.i("Getting directions")

        if (clientLocation.value != null && lastKnownLocation.value != null) {

            val lastKnownLocationString =
                lastKnownLocation.value!!.latitude.toString() + "," + lastKnownLocation.value!!.longitude.toString()
            val lastClientLocationString =
                clientLocation.value!!.latitude.toString() + "," + clientLocation.value!!.longitude.toString()

            val url: String =
                "https://maps.googleapis.com/maps/api/directions/json?origin=$lastKnownLocationString" +
                        "&destination=$lastClientLocationString&key=$MAPS_API_KEY&sensor=false&mode=driving"

            try {
                val path: MutableList<List<LatLng>> = ArrayList()
                val directionsRequest = object :
                    StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->

                        val jsonResponse = JSONObject(response)
                        Timber.i("Directions response: $jsonResponse")

                        // Get routes
                        val routes = jsonResponse.getJSONArray("routes")
                        val legs = routes.getJSONObject(0).getJSONArray("legs")
                        val steps = legs.getJSONObject(0).getJSONArray("steps")
                        for (i in 0 until steps.length()) {
                            val points =
                                steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                            path.add(PolyUtil.decode(points))
                        }
                        for (i in 0 until path.size) {
                            this.map!!.addPolyline(PolylineOptions().addAll(path[i])
                                .color(Color.BLACK))
                        }
                    }, Response.ErrorListener { _ ->
                        Timber.i("Getting Directions Failed")
                        Toast.makeText(activity, "Getting Directions Failed", Toast.LENGTH_SHORT).show()
                    }) {}
                val requestQueue = Volley.newRequestQueue(activity as RiderActivity)
                requestQueue.add(directionsRequest)

            } catch (ex: Exception) {
                Timber.i(ex)
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"

    }
}