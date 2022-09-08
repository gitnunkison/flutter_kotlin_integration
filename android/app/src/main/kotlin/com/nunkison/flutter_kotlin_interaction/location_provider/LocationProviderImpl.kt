package com.nunkison.flutter_kotlin_interaction.location_provider

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import io.flutter.embedding.android.FlutterActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("MissingPermission", "NewApi")
class LocationProviderImpl(
    private val app: Application,
    private val onPermissionRequest: (() -> Unit),
) : LocationProvider {

    private val locationManager = app.getSystemService(
        FlutterActivity.LOCATION_SERVICE
    ) as LocationManager

    override suspend fun getLocation(): Location =
        if (checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                getCurrentLocation()
            } else {
                getLocationFromRequestSingleUpdate()
            }
        } else {
            throw IllegalStateException("Você precisa autorizar o acesso a localização do celular.")
        }

    @SuppressLint("MissingPermission")
    private suspend fun getLocationFromRequestSingleUpdate(): Location =
        suspendCancellableCoroutine { cont ->
            locationManager.requestSingleUpdate(
                LocationManager.NETWORK_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        cont.resume(location, onCancellation = {
                            throw it
                        })
                    }

                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }, null
            )

            cont.invokeOnCancellation {
                throw it ?: IllegalStateException("Cancelado")
            }
        }

    private suspend fun getCurrentLocation(): Location =
        suspendCancellableCoroutine { cont ->
            locationManager.getCurrentLocation(
                LocationManager.NETWORK_PROVIDER,
                null,
                app.mainExecutor
            )
            {
                cont.resume(it, onCancellation = { t ->
                    throw t
                })
            }

            cont.invokeOnCancellation {
                throw it ?: IllegalStateException("Cancelado")
            }
        }

    private fun checkPermission(): Boolean =
        if (ContextCompat.checkSelfPermission(
                app,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionRequest.invoke()
            false
        } else {
            true
        }
}