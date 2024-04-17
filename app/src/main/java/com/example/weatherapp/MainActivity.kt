package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
        } else {
            Text("Loading or no data available.")
        }
    }
}