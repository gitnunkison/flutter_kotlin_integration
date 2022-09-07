package com.nunkison.flutter_kotlin_interaction

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.android.FlutterActivity

class LocationProvider(
    private val app: Application,
    private val onPermissionRequest: (() -> Unit)
) : LocationListener {

    private var locationManager: LocationManager? = null
    var isProvidingLocations = false

    var locationListener: ((location: Location) -> Unit)? = null


    override fun onLocationChanged(location: Location) {
        locationListener?.invoke(location)
    }

    fun start() {
        if (locationManager == null) {
            if (ContextCompat.checkSelfPermission(
                    app,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                onPermissionRequest.invoke()
                return
            }
        }
        if (!isProvidingLocations){
            locationManager = app.getSystemService(FlutterActivity.LOCATION_SERVICE) as LocationManager?
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    internal fun startLocationUpdates() {
        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            this
        )
        isProvidingLocations = true
    }
}