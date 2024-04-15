package com.example.weatherapp.network

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}