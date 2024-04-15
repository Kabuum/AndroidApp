package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.compose.ui.unit.dp
import com.example.weatherapp.network.RetrofitClient
import com.example.weatherapp.network.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import androidx.compose.runtime.*
import com.example.weatherapp.network.WeatherRepository
import kotlinx.coroutines.*



class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val _coordinates = MutableStateFlow<Pair<Double, Double>?>(null)
    val coordinates = _coordinates.asStateFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getCoordinates()
        val weatherData = mutableStateOf<WeatherResponse?>(null)
        setContent {
            WeatherAppTheme { // Define your Compose theme
                WeatherScreen(weatherData.value)
            }
        }
    }

    private fun getCoordinates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                _coordinates.value = Pair(it.latitude, it.longitude)
            }
        }
    }
    companion object {
        private const val PERMISSION_REQUEST_LOCATION = 100
    }
}

@Composable
fun DisplayLocation(latitude: String, longitude: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = latitude)
        Text(text = longitude)
    }
}
@Composable
fun WeatherScreen(weatherData: WeatherResponse? = null) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Weather Information")
        if (weatherData != null) {
            Text("Temperature: ${weatherData.temperature_2m} °C")
            Text("Humidity: ${weatherData.relative_humidity_2m} %")
            Text("Feels Like: ${weatherData.apparent_temperature} °C")
            Text("Cloud Cover: ${weatherData.cloud_cover} %")
            Text("Wind Speed: ${weatherData.wind_speed_10m} km/h")
            Text("Precipitation: ${weatherData.precipitation} mm")
            Text("Visibility: ${weatherData.visibility} meters")
        } else {
            Text("Loading or no data available.")
        }
    }
}