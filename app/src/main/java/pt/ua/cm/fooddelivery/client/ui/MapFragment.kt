package pt.ua.cm.fooddelivery.client.ui


import androidx.fragment.app.Fragment


class MapFragment : Fragment() {//, OnMapReadyCallback {

    /*
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

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(activity as MainActivity)

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
    }


    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(activity as MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(activity as MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
                locationResult.addOnCompleteListener(activity as MainActivity) { task ->
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

    private fun setRiderMarker() {
        if(lastRiderLocation != null) {
            map?.addMarker(MarkerOptions().position(lastRiderLocation!!))
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


            val url: String = "https://maps.googleapis.com/maps/api/directions/json?origin=$lastKnownLocationString" +
                    "&destination=$lastRiderLocationString&key=$MAPS_API_KEY&sensor=false&mode=driving"

            try {

                val path: MutableList<List<LatLng>> = ArrayList()
                //val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=10.3181466,123.9029382&destination=10.311795,123.915864&key=<YOUR_API_KEY>"
                val directionsRequest = object : StringRequest(Request.Method.GET, url, Response.Listener<String> {
                        response ->

                    val jsonResponse = JSONObject(response)

                    // Get routes
                    val routes = jsonResponse.getJSONArray("routes")
                    val legs = routes.getJSONObject(0).getJSONArray("legs")
                    val steps = legs.getJSONObject(0).getJSONArray("steps")
                    for (i in 0 until steps.length()) {
                        val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                        path.add(PolyUtil.decode(points))
                    }
                    for (i in 0 until path.size) {
                        this.map!!.addPolyline(PolylineOptions().addAll(path[i]).color(Color.BLACK))
                    }
                }, Response.ErrorListener {
                        _ ->
                }){}
                val requestQueue = Volley.newRequestQueue(activity as MainActivity)
                requestQueue.add(directionsRequest)

                /*Api.apiService.getDirections(url).enqueue(object :
                    Callback<Any> {
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        //handle error here
                        Timber.i("error $t")
                    }

                    override fun onResponse(
                        call: Call<Any>,
                        response: Response<Any>
                    ) {
                        Timber.i(response.toString())
                        Timber.i(response.body().toString())

                        Timber.i(response.body().toString())
                        //drawPolylines(response)

                        val jsonResponse = JSONObject(response.body().toString())

                        Timber.i("json resposne $jsonResponse")

                        // Get routes
                        val routes = jsonResponse.getJSONArray("routes")
                        val legs = routes.getJSONObject(0).getJSONArray("legs")
                        val steps = legs.getJSONObject(0).getJSONArray("steps")


                        Timber.i(routes.toString())

                        Timber.i(routes.getJSONObject(0).getJSONArray("legs").toString())



                    }
                })

                 */


            } catch (ex: Exception) {
                Timber.i(ex)
            }

        } else {
            Timber.i("FFFFFFFF")
        }
    }


    companion object {
        //private val TAG = MapsActivityCurrentPlace::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"

    }



     */









    /*
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        enableMyLocation()

        //These coordinates represent the latitude and longitude of the Googleplex.
        val latitude = 37.422160
        val longitude = -122.084270
        val zoomLevel = 15f

        val homeLatLng = LatLng(latitude, longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        map.addMarker(MarkerOptions().position(homeLatLng))
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            activity as MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        Timber.i("ola")
        if (isPermissionGranted()) {
            Timber.i("ya")
            map.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                activity as MainActivity,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

     */
}
    /*
    private lateinit var binding: FragmentMapBinding
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(activity as MainActivity)
        fetchLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        Timber.i("Fetching location")
        if (ActivityCompat.checkSelfPermission(
                activity as MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity as MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }

        Timber.i("....")
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location

                Timber.i("LOCATION: $currentLocation")
                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
                mapFragment!!.getMapAsync(this)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap.addMarker(markerOptions)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        }
        }
    }
}

     */