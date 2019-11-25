package com.twointerns.ridetracker.view.fragment.detail

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

import com.twointerns.ridetracker.R
import com.twointerns.ridetracker.databinding.DetailRideFragmentBinding
import com.twointerns.ridetracker.model.entity.LocationData
import com.twointerns.ridetracker.viewmodel.DetailRideViewModel
import kotlinx.android.synthetic.main.detail_ride_fragment.*

class DetailRideFragment : Fragment(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap

    private lateinit var binding: DetailRideFragmentBinding
    private lateinit var viewModel: DetailRideViewModel
    private lateinit var locationData: LocationData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_ride_fragment, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(DetailRideViewModel::class.java)

        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args: DetailRideFragmentArgs =
            DetailRideFragmentArgs.fromBundle(
                arguments!!
            )
        locationData=args.locationData
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.detail_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onMapReady(map: GoogleMap) {
        val startLatLng=LatLng(locationData.latlngList?.get(0)?.latitude!!,locationData.latlngList?.get(0)?.longitude!!)
        val startBitmap = BitmapFactory.decodeResource(resources, R.drawable.start_marker)
        val endBitmap = BitmapFactory.decodeResource(resources, R.drawable.end_marker)

        map.moveCamera(CameraUpdateFactory.newLatLng(startLatLng))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 16f))
        map.addPolyline(PolylineOptions()
            .addAll(locationData.latlngList)
            .clickable(false)
            .color(resources.getColor(R.color.green))
        )
        //add start Marker
        map.addMarker(MarkerOptions()
            .position(locationData.latlngList!!.first()))
            .setIcon( BitmapDescriptorFactory.fromBitmap(
                Bitmap.createScaledBitmap(startBitmap, 60,60, false)
            ))

        //add end Marker
        map.addMarker(MarkerOptions()
            .position(locationData.latlngList!!.last()))
            .setIcon( BitmapDescriptorFactory.fromBitmap(
                Bitmap.createScaledBitmap(endBitmap, 60,60, false)
            ))

    }

}
