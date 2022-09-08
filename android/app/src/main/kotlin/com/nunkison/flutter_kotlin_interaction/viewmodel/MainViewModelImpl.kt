package com.nunkison.flutter_kotlin_interaction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nunkison.flutter_kotlin_interaction.location_provider.LocationProvider

class MainViewModelImpl(
    private val locationProvider: LocationProvider
) : ViewModel(), MainViewModel {

    private val _latLng = MutableLiveData(LatLng(0.0, 0.0))
    override val latLng: LiveData<LatLng> = _latLng

    override suspend fun loadLocation() {
        locationProvider.getLocation().let {
            _latLng.postValue(
                LatLng(
                    lat = it.latitude,
                    lng = it.longitude
                )
            )
        }
    }
}



