package com.nunkison.flutter_kotlin_interaction.location_provider

import android.location.Location

interface LocationProvider {
    suspend fun getLocation(): Location
}