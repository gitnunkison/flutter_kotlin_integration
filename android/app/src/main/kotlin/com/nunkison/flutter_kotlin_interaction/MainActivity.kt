package com.nunkison.flutter_kotlin_interaction

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.nunkison.flutter_kotlin_interaction.viewmodel.LatLng
import com.nunkison.flutter_kotlin_interaction.viewmodel.MainViewModel
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.launch

class MainActivity : MainViewModelFlutterFragmentActivity() {

    private var methodChannelResult: MethodChannel.Result? = null

    override val vm: MainViewModel by viewModels { inject() }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call: MethodCall?, result: MethodChannel.Result? ->
            methodChannelResult = result
            when (call?.method) {
                "startLocationService" -> loadLocation()
                "getLocation" -> loadLocation()
                else -> result?.notImplemented()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.latLng.observe(this) {
            if (it.lat != 0.0 && it.lng != 0.0) {
                updateLocation(it)
                methodChannelResult = null
            } else {
                loadLocation()
            }
        }
    }

    override fun onPermissionDenied() {
        sendPermissionError()
        methodChannelResult = null
    }

    override fun onPermissionGranted() {
        loadLocation()
    }

    private fun loadLocation() {
        lifecycleScope.launch {
            try {
                vm.loadLocation()
            } catch (e: Exception) {}
        }
    }

    private fun updateLocation(latLng: LatLng) {
        methodChannelResult?.success(
            listOf(
                latLng.lat,
                latLng.lng
            )
        )
    }

    private fun sendPermissionError() {
        methodChannelResult?.error(
            "UNAVAILABLE",
            "Para capturar a localização, é necessária autorização.",
            null
        )
    }
}

const val CHANNEL = "com.nunkison.flutter/location"
