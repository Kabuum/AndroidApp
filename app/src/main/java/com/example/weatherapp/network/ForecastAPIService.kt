package com.example.weatherapp.network
import  retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://dmigw.govcloud.dk/v1/forecastedr"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)

interface WeatherApiService {
    @GET("/collections/harmonie_nea_sf/position")
    fun getForecast(
        @Query("coords") coordinates: String,
        @Query("crs") crs: String,
        @Query("parameter-name") parameterName: String,
        @Query("api-key") apiKey: String
        )
}
