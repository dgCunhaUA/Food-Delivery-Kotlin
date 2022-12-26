package pt.ua.cm.fooddelivery.client.ui

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import pt.ua.cm.fooddelivery.BuildConfig.MAPS_API_KEY
import pt.ua.cm.fooddelivery.ClientActivity
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.databinding.FragmentMapBinding
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber


class MapFragment : Fragment(), OnMapReadyCallback {

    private val args: MapFragmentArgs by navArgs()

    private lateinit var binding: FragmentMapBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var cameraPosition: CameraPosition? = null
    private var map: GoogleMap? = null

    private var lastRiderLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate with args: $args")

        lastRiderLocation = args.riderLocation

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity as ClientActivity)

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync(this)

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map

        // Prompt the user for permission.
        getLocationPermission()

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        setRiderMarker()

        //getDirections()
        startUpdates()
    }


    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(activity as ClientActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(activity as ClientActivity,
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
                lastKnownLocation = null
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
                locationResult.addOnCompleteListener(activity as ClientActivity) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        Timber.i("LAST KNOW LOCATION: $lastKnownLocation")
                        if (lastKnownLocation != null) {
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }

                        getDirections()
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


    private val markersList = ArrayList<Marker>()
    private fun setRiderMarker() {
        if (lastRiderLocation != null) {

            val marker: Marker? = map?.addMarker(MarkerOptions().position(lastRiderLocation!!))

            if (marker != null) {
                for (m: Marker in markersList) {
                    m.isVisible = false
                }
                markersList.add(marker)
            }
        }
    }

    private fun getDirections() {
        Timber.i("Getting directions")

        if (lastRiderLocation != null && lastKnownLocation != null) {

            val lastKnownLocationString =
                lastKnownLocation!!.latitude.toString() + "," + lastKnownLocation!!.longitude.toString()
            val lastRiderLocationString =
                lastRiderLocation!!.latitude.toString() + "," + lastRiderLocation!!.longitude.toString()


            Timber.i("origin: $lastKnownLocationString")
            Timber.i("dest: $lastRiderLocationString")
            Timber.i("leu $MAPS_API_KEY")


            val url: String =
                "https://maps.googleapis.com/maps/api/directions/json?origin=$lastKnownLocationString" +
                        "&destination=$lastRiderLocationString&key=$MAPS_API_KEY&sensor=false&mode=driving"

            try {

                val path: MutableList<List<LatLng>> = ArrayList()
                //val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=10.3181466,123.9029382&destination=10.311795,123.915864&key=<YOUR_API_KEY>"
                val directionsRequest = object :
                    StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->

                        val jsonResponse = JSONObject(response)

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
                    }) {}
                val requestQueue = Volley.newRequestQueue(activity as ClientActivity)
                requestQueue.add(directionsRequest)
            } catch (ex: Exception) {
                Timber.i(ex)
            }

        } else {
            Timber.i("Getting Directions Failed")
            showToast("Getting Directions Failed")
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private val scope = MainScope()
    var job: Job? = null
    private fun startUpdates() {
        job = scope.launch {
            while (true) {

                Api.apiService.getRiderLocation(args.orderId).enqueue(object :
                    Callback<DeliveriesResponse> {
                    override fun onFailure(call: Call<DeliveriesResponse>, t: Throwable) {
                        Timber.i("error $t")
                    }

                    override fun onResponse(
                        call: Call<DeliveriesResponse>,
                        response: retrofit2.Response<DeliveriesResponse>
                    ) {
                        val order: DeliveriesResponse? = response.body()

                        lastRiderLocation = LatLng(order?.rider_lat!!, order.rider_lng!!)
                        setRiderMarker()
                    }
                })

                delay(1000)
            }
        }
    }

    private fun stopUpdates() {
        job?.cancel()
        job = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopUpdates()
    }

    companion object {
        //private val TAG = MapsActivityCurrentPlace::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"

    }
}