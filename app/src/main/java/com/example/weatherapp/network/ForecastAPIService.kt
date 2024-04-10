package com.example.weatherapp.network
import  retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://dmigw.govcloud.dk/v1/forecastedr"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)

interface MarsApiService {
    @GET("Forecast")
    fun getForecast(): String
}
