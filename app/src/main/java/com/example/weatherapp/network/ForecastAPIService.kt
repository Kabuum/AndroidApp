package com.example.weatherapp.network
import android.util.Log
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Callback

interface OpenMeteoService {
    @GET("v1/forecast")
    fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourlyFields: String = "temperature_2m,relative_humidity_2m,apparent_temperature,cloud_cover,wind_speed_10m,precipitation,precipitation_probability,visibility,is_day,weather_code"
    ): Call<WeatherResponse>
}

data class WeatherResponse(
    @SerializedName("temperature_2m") val temperature_2m: Float,
    @SerializedName("relative_humidity_2m") val relative_humidity_2m: Int,
    @SerializedName("apparent_temperature") val apparent_temperature: Float,
    @SerializedName("cloud_cover") val cloud_cover: Int,
    @SerializedName("wind_speed_10m") val wind_speed_10m: Float,
    @SerializedName("precipitation") val precipitation: Float,
    @SerializedName("precipitation_probability") val precipitation_probability: Int,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("is_day") val is_day: Boolean,
    @SerializedName("weather_code") val weather_code: Int
)


object RetrofitClient {
    private const val BASE_URL = "https://api.open-meteo.com/"

    val service: OpenMeteoService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoService::class.java)
    }
}
class WeatherRepository {

    // Changed the method signature to accept Pair<Double, Double>
    fun fetchWeather(coordinates: Pair<Double, Double>, callback: (WeatherResponse?, Throwable?) -> Unit) {
        // Use the coordinates directly from the Pair
        Log.d("coordinate 2", "${coordinates}")
        RetrofitClient.service.getCurrentWeather(latitude = coordinates.first, longitude = coordinates.second).enqueue(object : Callback<WeatherResponse> {
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

