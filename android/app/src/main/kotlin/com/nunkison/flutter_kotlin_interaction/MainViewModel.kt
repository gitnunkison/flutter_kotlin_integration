package com.nunkison.flutter_kotlin_interaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

interface MainViewModel {
    fun startLocationProvider()

    val latlng: LiveData<LatLng>

    fun isProvidingLocations(): Boolean

    class Factory(
        private val locationProvider: LocationProvider
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(LocationProvider::class.java)) {
                return MainViewModelImpl(locationProvider) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}