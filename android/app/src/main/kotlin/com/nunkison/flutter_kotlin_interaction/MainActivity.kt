package com.nunkison.flutter_kotlin_interaction

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity(), LocationListener {

    private var locationManager: LocationManager? = null
    private var methodChannelResult: MethodChannel.Result? = null

    var lat = 0.0
    var lng = 0.0

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call: MethodCall?, result: MethodChannel.Result? ->
            methodChannelResult = result!!
            if (locationManager == null) {
                startLocationManager()
            }
            if (call!!.method.equals("getLocation")) {
                updateLocation()
            } else {
                result.notImplemented()
            }
        }
    }

    private fun startLocationManager() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            this
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> startLocationUpdates()
                PackageManager.PERMISSION_DENIED -> sendPermissionError()
            }
        }
    }

    private fun sendPermissionError() {
        methodChannelResult?.error(
            "UNAVAILABLE",
            "Para capturar a localização, é necessária autorização.",
            null
        )
        methodChannelResult = null
    }

    override fun onLocationChanged(location: Location) {
        lat = location.latitude
        lng = location.longitude
    }

    private fun updateLocation() {
        if (lat == 0.0 || lng == 0.0){
            methodChannelResult?.error(
                "UNAVAILABLE",
                "Localização ainda não disponível. Tente novamente dentro de alguns segundos.",
                null
            )
        } else {
            methodChannelResult?.success(listOf(lat, lng))
            methodChannelResult = null
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
        private const val CHANNEL = "com.nunkison.flutter/location"
    }
}
