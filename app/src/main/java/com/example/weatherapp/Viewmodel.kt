package com.example.weatherapp
import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.network.WeatherRepository
import com.example.weatherapp.network.WeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    private val weatherRepository = WeatherRepository()
    val weatherData = MutableStateFlow<WeatherResponse?>(null)

    init {
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        viewModelScope.launch {
            val location = getLocation()
            location?.let {
                val coordinates = Pair(it.latitude, it.longitude)
                Log.d("Coordinates", "${coordinates}")
                weatherRepository.fetchWeather(coordinates) { weatherResponse, error ->
                    if (error != null) {
                        // Handle error
                    } else {
                        weatherData.value = weatherResponse
                    }
                }
            }
        }
    }

    private suspend fun getLocation(): Location? {
        val cancellationTokenSource = CancellationTokenSource()

        // Use the new LocationRequest.Builder
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,60000)
            .build()
        return try {
            if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null // Return null when permissions are not available
            }
            // Proceed with getting the location
            fusedLocationClient.getCurrentLocation(locationRequest.priority, cancellationTokenSource.token).await()
        } catch (e: Exception) {
            // Handle any exceptions
            null
        }
    }
}
