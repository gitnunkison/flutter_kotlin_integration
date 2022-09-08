package com.nunkison.flutter_kotlin_interaction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nunkison.flutter_kotlin_interaction.location_provider.LocationProvider

interface MainViewModel {
    suspend fun loadLocation()

    val latLng: LiveData<LatLng>

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