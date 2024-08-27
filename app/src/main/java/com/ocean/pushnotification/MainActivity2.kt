package com.ocean.pushnotification

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ocean.pushnotification.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    lateinit var locationService: LocationService
    var currentLocation: Location? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationService.removeLocation()
        stopService(Intent(this, ForeGroundService::class.java))
    }

    private fun onClickListener(){
        binding.btnFetchLocation.setOnClickListener {
            if (!checkLocationPermission()) {
                fetchLocation()
            } else {
                requestLocationPermission()
            }
        }
        binding.btnFetchLocationFservice.setOnClickListener {
            ContextCompat.startForegroundService(this, Intent(this, ForeGroundService::class.java))
        }
    }

    fun fetchLocation() {
        try {
            locationService = LocationService(
                this, locationUpdate = object : LocationService.LocationUpdate {
                    override fun getLocation(location: Location?, msg: String) {
                        Log.d("location", "${location?.latitude} : ${location?.longitude}")
                        currentLocation = location
                        binding.textView.text =
                            "${currentLocation?.longitude} : ${currentLocation?.latitude}"
                    }
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        locationService.connectLocation()
    }

    private fun checkLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            } else {
                Toast.makeText(this, "Location permission is needed", Toast.LENGTH_SHORT).show()
            }
        }
    }


}