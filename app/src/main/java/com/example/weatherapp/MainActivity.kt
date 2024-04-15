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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.weatherapp.network.RetrofitClient
import com.example.weatherapp.network.WeatherRepository
import com.example.weatherapp.network.WeatherResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude by mutableStateOf("Waiting for location...")
    private var longitude by mutableStateOf("Waiting for location...")
    override fun onCreate(savedInstanceState: Bundle?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationPermission()
        super.onCreate(savedInstanceState)
        val weatherData = mutableStateOf<WeatherResponse?>(null)
        
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(

                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen()
                    DisplayLocation(latitude, longitude)
                }
            }
        }
    }
    private fun fetchWeather(latitude: Double, longitude: Double) {
        RetrofitClient.webservice.getCurrentWeather(latitude, longitude).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    // Update the UI with the weather data
                    val weatherData = response.body()
                    runOnUiThread {
                        // Assuming you have TextViews or other UI elements to update
                        //findViewById<TextView>(R.id.temperatureTextView).text = "Temperature: ${weatherData?.temperature_2m}"
                    }
                } else {
                    // Handle unsuccessful response
                    Log.e("API_ERROR", "Response not successful")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                // Handle failure to make or process the call
                Log.e("API_ERROR", "Failed to fetch weather data", t)
            }
        })
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_REQUEST_LOCATION
            )
        } else {
            getCoords()
        }
    }
    private fun getCoords(coordinates: MutableState<Pair<Double, Double>?>) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permissions aren't granted, perhaps notify the user or handle accordingly
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                latitude = "${location.latitude}"
                longitude = "${location.longitude}"
            } else {
                latitude = "Location not available"
                longitude = "Location not available"
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_LOCATION = 100
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
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
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        Greeting("Android",)
    }
}