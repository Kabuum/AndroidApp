package com.example.weatherapp
import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.network.RetrofitClient
import com.example.weatherapp.network.WeatherData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    var weatherData = MutableStateFlow<WeatherData?>(null)

    init {
        fetchWeather()
    }
    fun fetchWeather() {
        // Use the coordinates directly from the Pair
        viewModelScope.launch {
            val result = RetrofitClient.service.getCurrentWeather()
            val jsonObject: JSONObject = JSONObject(result)
            val current = jsonObject.getJSONObject("current")

            val _weatherData = WeatherData(
                temperature_2m = current.getDouble("temperature_2m").toFloat(),
                relative_humidity_2m = current.getInt("relative_humidity_2m"),
                cloud_cover = current.getInt("cloud_cover"),
                wind_speed_10m = current.getDouble("wind_speed_10m").toFloat(),
                precipitation = current.getDouble("precipitation").toFloat(),
                is_day = when (current.getInt("is_day")) {
                    1 -> true
                    else -> false
                },
                weather_code = (current.getInt("weather_code"))
            )
            weatherData.value = _weatherData
        }
    }
}
