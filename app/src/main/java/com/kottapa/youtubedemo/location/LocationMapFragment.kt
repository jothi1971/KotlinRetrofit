package com.kottapa.youtubedemo.location



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.kottapa.youtubedemo.R


class LocationMapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        const val TAG = "LocationMapFragment"
    }

    private val viewModel by activityViewModels<LocationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_locationmap, container, false)

        //mapFragment can be null
        val mapFragment =  childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment

        mapFragment?.getMapAsync(this)

        return view
    }


    override fun onMapReady(myMap: GoogleMap?) {
        Log.v(TAG,"jothi mapready called")

        //Enable user location button
        myMap?.isMyLocationEnabled = true

        //Enable zoom control
        myMap?.uiSettings?.isZoomControlsEnabled = true

        //set map type
        myMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN

        //street level zoom
        val zoomLevel = 15f

        //observe location change
        viewModel.myLatitude.observe(viewLifecycleOwner, Observer {newLat ->
            Log.v(TAG,"jothi map location change ")

            //location can be null
            val newLatLng = LatLng(viewModel.myLatitude.value?:0.0, viewModel.myLongitude.value?:0.0)
            myMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, zoomLevel))
        })

    }



}

