package com.example.weatherapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider



class MainActivity : ComponentActivity() {
    private val _coordinates = MutableStateFlow<Pair<Double, Double>?>(null)
    private val ViewModel by viewModels<WeatherViewModel>(factoryProducer = {object: ViewModelProvider.Factory{
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return WeatherViewModel(application) as T
        }
    }})
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                //CurrentWeatherDisplay(viewModel = ViewModel, modifier = Modifier)
                UI(viewModel = ViewModel, backgroundColor = Color.White, modifier = Modifier)
            }
        }
    }

}
@Composable
fun WeatherScreen2(viewModel: WeatherViewModel) {
    var state = viewModel.weatherData.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Weather Information")
        if (state.value != null) {
            Text("Temperature: ${state.value?.temperature_2m} °C")
            Text("Humidity: ${state.value?.relative_humidity_2m}%")
            Text("Cloud Cover ${state.value?.cloud_cover}")
            Text("Precipitation ${state.value?.precipitation}")
            Text("Day? ${state.value?.is_day}")
            Text("Weather Code ${state.value?.weather_code}")
        } else {
            Text("Loading or no data available.")
        }
    }
}
@Composable
fun CurrentWeatherDisplay(viewModel: WeatherViewModel, modifier: Modifier){
    var state = viewModel.weatherData.collectAsState()
    Surface(modifier = Modifier.fillMaxSize(), color = getBackgroundColor(state.value?.weather_code)) {
        Box(modifier = Modifier.padding(
            start = 150.dp,
            top = 50.dp,
            end = 50.dp,
            bottom = 50.dp)
        ) {
            Column {
                Image(painter = painterResource(id = convertWeatherCodeToImage(state.value?.weather_code)), contentDescription = convertWeatherCodeToString(state.value?.weather_code), modifier.scale(3f,2f))
                Spacer(modifier = Modifier.height(60.dp))
                Text(text = "${state.value?.temperature_2m}°C", textAlign = TextAlign.Center, modifier = Modifier.offset((10).dp))
                Text(text =  convertWeatherCodeToString(state.value?.weather_code), textAlign = TextAlign.Center, modifier = Modifier.offset((-20).dp))
                Text(text = "Cloud coverage: ${state.value?.cloud_cover}%", textAlign = TextAlign.Center, modifier = Modifier.offset((-50).dp))
                Text(text = "Precipitation: ${state.value?.precipitation} mm", textAlign = TextAlign.Center, modifier = Modifier.offset((-50).dp))
                Text(text = "Humidity: ${state.value?.relative_humidity_2m}%", textAlign = TextAlign.Center, modifier = Modifier.offset((-30).dp))
                Text(text = "Wind speed: ${state.value?.wind_speed_10m} m/s", textAlign = TextAlign.Center, modifier = Modifier.offset((-50).dp))
            }
        }
    }
}

@Composable
fun UI (viewModel: WeatherViewModel, backgroundColor: Color, modifier: Modifier = Modifier) {
    var state = viewModel.weatherData.collectAsState()
    Card(
        backgroundColor = getBackgroundColor(state.value?.weather_code),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(16.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(painter = painterResource(id = convertWeatherCodeToImage(state.value?.weather_code)), contentDescription = convertWeatherCodeToString(state.value?.weather_code), Modifier.scale(2f,2f))
            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material.Text(
                text = "${state.value?.temperature_2m}°C",
                fontSize = 50.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = convertWeatherCodeToString(state.value?.weather_code),
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Image( painterResource(id = R.drawable.humidity_percentage_wght400_opsz48), contentDescription = null, Modifier.scale(1.5f).offset(5.dp, 2.dp))
                Text(text = "${state.value?.relative_humidity_2m} %")
                Image(painterResource(id = R.drawable.air_wght400_opsz48), contentDescription = null, Modifier.scale(1.5f).offset(5.dp,2.dp))
                Text(text = "${state.value?.wind_speed_10m} km/h")
            }
        }
    }
}
fun convertWeatherCodeToImage(weatherCode: Int?): Int{
    val weatherCodeConverted = when(weatherCode){
        0-> R.drawable.sunny_48px
        1,2 -> R.drawable.partly_cloudy_day_48px
        3 -> R.drawable.cloud_48px
        51, 53, 55, 56, 57, 61, 63 ,65, 66, 67 -> R.drawable.rainy_48px
        45, 48 -> R.drawable.foggy_48px
        else -> R.drawable.cloud_48px
    }
    return weatherCodeConverted
}
fun convertWeatherCodeToString(weatherCode: Int?): String{
    val weatherCodeConverted = when(weatherCode){
        0 -> "Clear Sky"
        1 -> "Mainly Clear"
        2 -> "Partly Cloudy"
        3 -> "Overcast"
        45 -> "Fog"
        48 -> "Depositing Rime Fog"
        51 -> "Light Drizzle"
        53 -> "Moderate Drizzle"
        55 -> "Dense Drizzle"
        56 -> "Freezing Light Drizzle"
        57 -> "Freezing Dense Drizzle"
        61 -> "Slight Rain"
        63 -> "Moderate Rain"
        65 -> "Heavy Rain"
        66 -> "Freezing Light Rain"
        67 -> "Freezing Heavy Rain"
        71 -> "Slight Snow Fall"
        73 -> "Moderate Snow Fall"
        75 -> "Heavy Snow Fall"
        77 -> "Snow Grains"
        80 -> "Slight Rain Showers"
        81 -> "Moderate Rain Showers"
        82 -> "Violent Rain Showers"
        85 -> "Slight Snow Shower"
        86 -> "Heavy Snow Shower"
        95 -> "Thunderstorm"
        96 -> "Thunderstorm With Slight Hail"
        99 -> " Thunderstorm With Heavy Hail"
        else -> "Cloudy"
    }
    return weatherCodeConverted
}
fun getBackgroundColor(weatherCode: Int?): Color {
    return when (weatherCode) {
        0 -> Color(0xFFFFE0B2)
        1,2,3 -> Color(0xFFCED4E0)
        51, 53, 55, 56, 57, 61, 63 ,65, 66, 67 -> Color(0xFF90B2F7)
        45, 48 -> Color(0xFF949597)
        else -> Color(0xFFFFE0B2)
    }
}