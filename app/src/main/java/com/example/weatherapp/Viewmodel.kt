package com.example.weatherapp
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.network.RetrofitClient
import com.example.weatherapp.network.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

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