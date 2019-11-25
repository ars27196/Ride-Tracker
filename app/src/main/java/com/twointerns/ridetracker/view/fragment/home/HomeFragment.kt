package com.twointerns.ridetracker.view.fragment.home

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.twointerns.ridetracker.R
import com.twointerns.ridetracker.databinding.FragmentHomeBinding
import com.twointerns.ridetracker.viewmodel.HomeViewModel
import com.twointerns.ridetracker.utils.PermissionUtil
import android.graphics.Bitmap
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.RequestResult
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.util.DirectionConverter
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place

import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.*
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ListHolder
import com.twointerns.ridetracker.utils.GlobalUtils
import com.twointerns.ridetracker.view.adapter.DialogListAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var markerOptions: MarkerOptions
    private lateinit var lastMarker: Marker
    private lateinit var currentLatLng: LatLng
    private lateinit var listOfNearbyPlaces: ArrayList<String>
    private lateinit var listOfNearbyPlacesLatLng: ArrayList<LatLng>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        ) as FragmentHomeBinding
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        binding.viewModel = homeViewModel
//        binding.searchBar.clearFocus()
//        binding.searchBar2.clearFocus()
        mapFragment.getMapAsync(this)
        val locationButton =
            (mapFragment.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(
                Integer.parseInt("2")
            )

        //rearranging the center at current location button
        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 30, 280)

        /* binding.searchBar2.setOnFocusChangeListener { v, hasFocus ->
             if (search_bar2.hasFocus()) {
                 launchExpandableDialogBox()

             }
         }*/
        setupEndAutoCompleteFragment()
        setupStartAutoCompleteFragment()

        binding.trafficImage.setOnClickListener {
            if (GlobalUtils.trafficEnabled) {
                mMap.isTrafficEnabled = false
                GlobalUtils.trafficEnabled = false

            } else {
                mMap.isTrafficEnabled = true
                GlobalUtils.trafficEnabled = true
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.serviceRunning.observe(this, Observer {
            if (it) {
                binding.startTracking.text = "Stop Tracking"
            } else {
                binding.startTracking.text = "Start Tracking"
            }
        })
    }

    private fun launchExpandableDialogBox() {
        val adapter = DialogListAdapter(context!!, 5)
        val builder = DialogPlus.newDialog(context!!).apply {
            setContentHolder(ListHolder())

            val header = R.layout.list_header
            setHeader(header)
            isCancelable = true
            setGravity(Gravity.BOTTOM)
            isExpanded = true
            setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
            //setContentWidth(800)
            setOnCancelListener { dialog -> toast(context, "cancelled") }
            overlayBackgroundResource = android.R.color.transparent
            setAdapter(adapter)

            setOnClickListener { dialog, view ->
                if (view is TextView) {
                    toast(context, view.text.toString())
                }
            }
            setOnItemClickListener { dialog, item, view, position ->
                val textView = view.findViewById<TextView>(R.id.text_view)
                toast(context, textView.text.toString())
            }


        }

        builder.create().show()
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        mMap = googleMap!!
        val lo = LatLng(-33.852, 151.211)

        googleMap.uiSettings?.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lo))

        //  googleMap.setOnMapClickListener(this)

        if (PermissionUtil.isLocationPermissionGiven(activity!!)) {
            googleMap.isMyLocationEnabled = true
            getCurrentLocation()

        } else {
            PermissionUtil.requestLocationPermission(this)
        }
    }

    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                lastLocation = it
                currentLatLng = LatLng(it.latitude, it.longitude)


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    private fun getCurrentPlace() {
        listOfNearbyPlaces = ArrayList()
        listOfNearbyPlacesLatLng = ArrayList()
        val placesClient = Places.createClient(context!!)
        val placeFields = listOf(Place.Field.NAME, Place.Field.LAT_LNG)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)
        if (ContextCompat.checkSelfPermission(
                context!!,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful()) {
                    val response = it.getResult()
                    for (placeLikelihood: PlaceLikelihood in response!!.getPlaceLikelihoods()) {

                        listOfNearbyPlaces.add((placeLikelihood.place.name)!!)
                        listOfNearbyPlacesLatLng.add((placeLikelihood.place.latLng)!!)
                    }
//                    val arrayAdapter = ArrayAdapter(
//                        context!!,
//                        android.R.layout.simple_spinner_item,
//                        listOfNearbyPlaces
//                    )
//


                    /* binding.searchBar.adapter = arrayAdapter
                     binding.searchBar.onItemSelectedListener =
                         object : AdapterView.OnItemSelectedListener,
                             AdapterView.OnItemClickListener {
                             override fun onItemSelected(
                                 parent: AdapterView<*>?,
                                 view: View?,
                                 position: Int,
                                 id: Long
                             ) {
                                 currentLatLng = listOfNearbyPlacesLatLng[position]

                             }

                             override fun onItemClick(
                                 parent: AdapterView<*>?,
                                 view: View?,
                                 position: Int,
                                 id: Long
                             ) {
                                 TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                             }

                             override fun onNothingSelected(parent: AdapterView<*>?) {
                                 TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                             }
                         }*/


                } else {
                    val exception = it.getException()
                    Toast.makeText(context, "Places Faliure: " + exception, Toast.LENGTH_LONG)
                        .show()
                }
            })

        } else {
            // A local method to request required permissions
            // See https://developer.android.com/training/permissions/requesting
//    getLocationPermission()
        }
    }


    private fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun placeMarkerOnMap(location: LatLng) {

        markerOptions = MarkerOptions().position(location)

        val height = 100
        val width = 100
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.current_location)

        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
                Bitmap.createScaledBitmap(bitmap, width, height, false)
            )
        )
//        val titleStr = getAddress(location)  // add these two lines
//        val value=titleStr
//        markerOptions.title(titleStr)
        lastMarker = mMap.addMarker(markerOptions)

    }


    var polyLine: Polyline? = null
    private fun drawRoute(origin: LatLng, destination: LatLng) {

        GoogleDirection.withServerKey("AIzaSyABisSGwdgHRDoZUwNoW3iTi0QFHRCRHTA")

            .from(origin)
            .to(destination)
            .execute(object : DirectionCallback {
                override fun onDirectionSuccess(direction: Direction?) {
                    val status = direction?.status
                    if (status?.equals(RequestResult.OK)!!) {
                        Toast.makeText(context, "success", Toast.LENGTH_LONG).show()

                        val route = direction?.routeList?.get(0)
                        val leg = route?.legList?.get(0)
                        val polylineOptions = DirectionConverter.createPolyline(
                            context!!,
                            leg?.directionPoint,
                            5,
                            Color.RED
                        )
                        polyLine = mMap.addPolyline(polylineOptions)
                    } else if (status.equals(RequestResult.NOT_FOUND)) {
                        Toast.makeText(context, "faliure", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onDirectionFailure(t: Throwable?) {
                    Toast.makeText(context, "total failure", Toast.LENGTH_LONG).show()

                }

            })
    }


    private fun setupEndAutoCompleteFragment() {
        if (!Places.isInitialized()) {
            Places.initialize(context!!, GlobalUtils.ApiKey, Locale.US)
        }
        var fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS
        )

        val autocompleteFragment: AutocompleteSupportFragment =
            childFragmentManager.findFragmentById(R.id.place_end_autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(fields)
        autocompleteFragment.setCountry("PAK")

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {

            override fun onPlaceSelected(place: Place) {
                Toast.makeText(context!!, "sucess: " + place.address.toString(), Toast.LENGTH_LONG)
                    .show()
                if (polyLine == null) {
                    drawRoute(
                        currentLatLng,
                        place.latLng!!
                    )
                } else {
                    polyLine!!.remove()
                    drawRoute(
                        currentLatLng,
                        place.latLng!!
                    )
                }
            }

            override fun onError(status: Status) {
                Toast.makeText(context!!, "No Location Selected", Toast.LENGTH_LONG).show()

            }
        })

    }

    private fun setupStartAutoCompleteFragment() {
        if (!Places.isInitialized()) {
            Places.initialize(context!!, GlobalUtils.ApiKey, Locale.US)
        }
        var fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS
        )

        val autocompleteFragment: AutocompleteSupportFragment =
            childFragmentManager.findFragmentById(R.id.place_start_autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(fields)
        autocompleteFragment.setCountry("PAK")

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                currentLatLng = place.latLng!!
            }

            override fun onError(p0: Status) {
                Toast.makeText(context!!, "No Location Selected", Toast.LENGTH_LONG).show()
                getCurrentLocation()
            }
        })
    }
}