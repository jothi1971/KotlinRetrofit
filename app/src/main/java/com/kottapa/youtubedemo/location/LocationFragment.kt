package com.kottapa.youtubedemo.location

import android.Manifest
import android.content.pm.PackageManager
import com.kottapa.youtubedemo.detail.DetailViewModel
import com.kottapa.youtubedemo.detail.DetailViewModelFactory




import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kottapa.youtubedemo.R
import com.kottapa.youtubedemo.databinding.FragmentDetailBinding
import com.kottapa.youtubedemo.databinding.FragmentLocationBinding
import com.kottapa.youtubedemo.databinding.FragmentTopBinding
import com.kottapa.youtubedemo.login.LoginViewModel

const val REQUEST_LOCATION_PERMISSION_CODE = 11

class LocationFragment : Fragment() {

    companion object {
        const val TAG = "LocationFragment"
    }

    private lateinit var binding: FragmentLocationBinding

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by activityViewModels<LocationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_location, container, false)
        binding.locationViewModel = viewModel
        binding.setLifecycleOwner(this)

        //observe location permission
        viewModel.locationPermissionRequired.observe(viewLifecycleOwner, Observer {permissionFlag ->
            if ( permissionFlag ) {
                requestPermission()
            }
        })

        binding.viewMapButton.setOnClickListener(){
            Log.v(TAG,"jothi location value " +viewModel.myLatitude.value)


            if (PermissionChecker.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PermissionChecker.PERMISSION_GRANTED
            ) {
                this.findNavController().navigate(LocationFragmentDirections.actionLocationFragmentToGoogleMapsFragment())

            } else {
                requestPermission()
            }


        }

        return binding.root
    }

    fun requestPermission() {

        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION)){
            Log.v(TAG,"jothi location ratinale displayed ")

            Snackbar.make(
                binding.locationLayout,
                R.string.fine_location_permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                  requestLocationPermission()
                }
                .show()
        } else {
            requestLocationPermission()
        }
    }

    fun requestLocationPermission() {
        Log.v(TAG,"jothi request location permission " )

        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        requestPermissions(
            permissionsArray,
            REQUEST_LOCATION_PERMISSION_CODE
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            REQUEST_LOCATION_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.v(TAG,"jothi location permission granted " )
                    viewModel.startLocationUpdates()
                } else {
                    //Respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }


    }


}