package com.nunkison.flutter_kotlin_interaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModelImpl(
    private val locationProvider: LocationProvider
) : ViewModel(), MainViewModel {

    private val _latlng = MutableLiveData(LatLng(0.0, 0.0))
    override val latlng: LiveData<LatLng> = _latlng

    init {
        locationProvider.locationListener = { location ->
            _latlng.postValue(
                LatLng(
                    lat = location.latitude,
                    lng = location.longitude
                )
            )
        }
    }

    override fun startLocationProvider() {
        locationProvider.start()
    }

    override fun isProvidingLocations() = locationProvider.isProvidingLocations
}



