package com.nunkison.flutter_kotlin_interaction

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.nunkison.flutter_kotlin_interaction.location_provider.LocationProviderImpl
import com.nunkison.flutter_kotlin_interaction.viewmodel.MainViewModel
import io.flutter.embedding.android.FlutterFragmentActivity
import kotlinx.coroutines.launch

abstract class MainViewModelFlutterFragmentActivity: FlutterFragmentActivity() {
    abstract val vm: MainViewModel
    abstract fun onPermissionDenied()
    abstract fun onPermissionGranted()

    protected fun inject() = MainViewModel.Factory(
        LocationProviderImpl(
            application,
            onPermissionRequest = {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        )
    )

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.isEmpty()){
                onPermissionDenied()
                return
            }
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> onPermissionGranted()
                PackageManager.PERMISSION_DENIED -> onPermissionDenied()
            }
        }
    }
}

const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100