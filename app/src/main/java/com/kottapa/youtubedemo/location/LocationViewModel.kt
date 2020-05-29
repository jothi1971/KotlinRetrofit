package com.kottapa.youtubedemo.location

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.*
import androidx.lifecycle.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.kottapa.youtubedemo.network.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.security.KeyStore


private const val TAG = "LocationViewModel"

//viewmodel to get user location. Needs application context
class LocationViewModel(application: Application) : AndroidViewModel(application) {


    private val context = getApplication<Application>().applicationContext

    private val _myLatitude = MutableLiveData<Double>()
    val myLatitude: LiveData<Double>
        get() = _myLatitude

    private val _myLongitude = MutableLiveData<Double>()
    val myLongitude: LiveData<Double>
        get() = _myLongitude

    private val _latitudeString = MutableLiveData<String>()
    val latitudeString: LiveData<String>
        get() = _latitudeString

    private val _longitudeString = MutableLiveData<String>()
    val longitudeString: LiveData<String>
        get() = _longitudeString


    init {
        _latitudeString.value = "Latitude : " + "NA"
        _longitudeString.value = "Longitude : " + "NA"

    }

    // LiveData to display permission UI
    private val _locationPermissionRequired = MutableLiveData<Boolean>()
    val locationPermissionRequired: LiveData<Boolean>
        get() = _locationPermissionRequired



    //location stuff
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 10000  //10 seconds
        fastestInterval = 5000  //5 seconds
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY //GPS accuracy
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                Log.v(TAG,"jothi location received " +location)

                _myLatitude.value = location.latitude
                _myLongitude.value = location.longitude

                _latitudeString.value = "Latitude : " + location.latitude.toString()
                _longitudeString.value ="Longitude : " + location.longitude.toString()
            }
        }
    }


    fun startLocationUpdates(){

        if (checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) ==
            PERMISSION_GRANTED) {
            Log.v(TAG,"jothi started location updates ")

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
            _locationPermissionRequired.value = false
        } else {
            _locationPermissionRequired.value = true
        }

    }


    fun stopLocationUpdates(){

        if (checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) ==
            PERMISSION_GRANTED) {
            Log.v(TAG,"jothi stop location updates ")

            fusedLocationClient.removeLocationUpdates(locationCallback)
            
            _locationPermissionRequired.value = false
        } else {
            _locationPermissionRequired.value = true
        }

    }

    //remove updates when app closed
    override fun onCleared() {
        super.onCleared()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.v(TAG,"jothi remove location updates ")

    }
}

