package com.example.weatherapp.network
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


interface OpenMeteoService {
    @GET("/v1/forecast?latitude=52.52&longitude=13.41&current=temperature_2m,relative_humidity_2m,is_day,precipitation,cloud_cover,wind_speed_10m")
    suspend fun getCurrentWeather(
    ): String
}

data class WeatherData(
    val temperature_2m: Float,
    val relative_humidity_2m: Int,
    val cloud_cover: Int,
    val wind_speed_10m: Float,
    val precipitation: Float,
    val is_day: Boolean,
)

object RetrofitClient {
    private val BASE_URL = "https://api.open-meteo.com/"

    val service: OpenMeteoService by lazy {
        Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(OpenMeteoService::class.java)
    }
}

