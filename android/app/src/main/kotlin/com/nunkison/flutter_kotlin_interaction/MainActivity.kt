package com.nunkison.flutter_kotlin_interaction

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterFragmentActivity() {
    private val vm: MainViewModel by viewModels {
        buildMainViewModel()
    }

    private var methodChannelResult: MethodChannel.Result? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.latlng.observe(this) {
            if (it.lat != 0.0 && it.lng != 0.0) {
                updateLocation(it)
            } else {
                if (vm.isProvidingLocations()) {
                    emitError()
                } else {
                    vm.startLocationProvider()
                }
            }
        }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call: MethodCall?, result: MethodChannel.Result? ->
            methodChannelResult = result
            when (call?.method) {
                "startLocationService" -> vm.startLocationProvider()
                "getLocation" -> vm.startLocationProvider()
                else -> result?.notImplemented()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> vm.startLocationProvider()
                PackageManager.PERMISSION_DENIED -> sendPermissionError()
            }
        }
    }

    private fun emitError() {
        methodChannelResult?.error(
            "UNAVAILABLE",
            "Localização ainda não disponível. Tente novamente dentro de alguns segundos.",
            null
        )
        methodChannelResult = null
    }

    private fun updateLocation(latLng: LatLng) {
        methodChannelResult?.success(
            listOf(
                latLng.lat,
                latLng.lng
            )
        )
        methodChannelResult = null
    }

    private fun sendPermissionError() {
        methodChannelResult?.error(
            "UNAVAILABLE",
            "Para capturar a localização, é necessária autorização.",
            null
        )
        methodChannelResult = null
    }

    private fun buildMainViewModel() = MainViewModel.Factory(
        LocationProvider(
            app = application,
            onPermissionRequest = {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MainActivity.PERMISSION_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        ),
    )

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
        private const val CHANNEL = "com.nunkison.flutter/location"
    }
}
