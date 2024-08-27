package com.ocean.pushnotification

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.Manifest
import android.os.Looper


class LocationService @Inject constructor(
    val context: Context,
    val locationUpdate: LocationUpdate
) {
    val TAG = LocationService::class.java.simpleName
    private val PERMISSION_DENIED = "permission_denied"
    private val LOCATION_SUCCESS = "location_found"

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private var currentLocation: Location? = null

    init {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

    interface LocationUpdate {
        fun getLocation(location: Location?, msg: String)
    }

    private fun createLocationRequest(): LocationRequest {

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            TimeUnit.SECONDS.toMillis(30)
        ).setWaitForAccurateLocation(true).build()

        return locationRequest
    }

    private var locationCallback = object : LocationCallback(){

        override fun onLocationAvailability(p0: LocationAvailability) {
            super.onLocationAvailability(p0)
        }

        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            Log.d(TAG, "location found lat = ${locationResult.lastLocation?.latitude} & long = ${locationResult.lastLocation?.longitude}")
            currentLocation = locationResult.lastLocation
            locationUpdate.getLocation(currentLocation, LOCATION_SUCCESS)
        }
    }

    fun connectLocation(){
        if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
            ){
                locationUpdate.getLocation(null, PERMISSION_DENIED)
            return
        }else{
            Log.d(TAG, "location requested")
            fusedLocationProviderClient?.requestLocationUpdates(
                createLocationRequest(), locationCallback, Looper.getMainLooper()
            )
        }
    }

    fun removeLocation(){
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }
}