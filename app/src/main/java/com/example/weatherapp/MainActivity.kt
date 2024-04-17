package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel


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
            WeatherAppTheme { // Define your Compose theme
                WeatherScreen2(ViewModel)
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
    Surface(modifier = Modifier.fillMaxSize(), color ) {
        Box {
            Column {
                //Image(painter = , contentDescription = "")
                Text(text = "${state.value?.temperature_2m}°C", modifier = Modifier)
            }
        }
    }
}