package com.ocean.pushnotification

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

class LocationHelper {
    var LOCATION_REFRESH_TIME = 3000 // 3 seconds. The Minimum Time to get location update
    var LOCATION_REFRESH_DISTANCE = 0 // 0 meters. The Minimum Distance to be changed to get location update

    @SuppressLint("MissingPermission")
    fun startListeningUserLocation(context: Context, myLocationListener: MyLocationListener){
        val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener: LocationListener = object : LocationListener{
            override fun onLocationChanged(location: Location) {
                myLocationListener.onLocationChanged(location)
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }
        mLocationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_REFRESH_TIME.toLong(),
            LOCATION_REFRESH_DISTANCE.toFloat(),
            locationListener
        )
    }
}
interface MyLocationListener {
    fun onLocationChanged(location: Location?)
}