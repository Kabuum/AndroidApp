package com.example.weatherapp.network
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Callback
interface OpenMeteoService {
    @GET("v1/forecast")
    fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("temperature_unit") temperatureUnit: String = "celsius",
        @Query("wind_speed_unit") windSpeedUnit: String = "kmh",
        @Query("precipitation_unit") precipitationUnit: String = "mm",
        @Query("timezone") timezone: String = "auto",
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,apparent_temperature,cloud_cover,wind_speed_10m,precipitation,precipitation_probability,visibility,is_day,weather_code"
    ): Call<WeatherResponse>
}

data class WeatherResponse(
    val temperature_2m: Float,
    val relative_humidity_2m: Int,
    val apparent_temperature: Float,
    val cloud_cover: Int,
    val wind_speed_10m: Float,
    val precipitation: Float,
    val precipitation_probability: Int,
    val visibility: Int,
    val is_day: Boolean,
    val weather_code: Int
)


object RetrofitClient {
    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoService::class.java)
    }
}
class WeatherRepository {

    // Changed the method signature to accept Pair<Double, Double>
    fun fetchWeather(coordinates: Pair<Double, Double>, callback: (WeatherResponse?, Throwable?) -> Unit) {
        // Use the coordinates directly from the Pair
        RetrofitClient.webservice.getCurrentWeather(latitude = coordinates.first, longitude = coordinates.second).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, RuntimeException("Response not successful"))
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }
}

