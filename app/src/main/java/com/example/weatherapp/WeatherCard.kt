package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun WeatherCard(currentWeather: WeatherData) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)

    ) {
        Box(modifier = Modifier.background(color = getBackgroundColor(currentWeather.condition))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row( // Add a Row to place the icon and temperature next to each other
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Display weather icon based on condition
                    val weatherImageId = getWeatherImage(currentWeather.condition)
                    Image(
                        painter = painterResource(id = weatherImageId),
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(48.dp) // Adjust icon size as needed
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Add spacing between icon and text
                    Text(
                        text = "°C" + currentWeather.temperature,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "The current weather is: " + currentWeather.condition,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

data class WeatherData(val temperature: Int, val condition: String)

fun getBackgroundColor(condition: String): Color {
    return when (condition) {
        "Sunny" -> Color(0xFFFFE0B2)
        "Cloudy" -> Color(0xFFCFD2D8)
        "Rainy" -> Color(0xFFC2C5CB)
        else -> Color(0xFFFFE0B2)
    }
}

fun getWeatherImage(condition: String): Int {
    return when (condition){
        "Sunny" ->  R.drawable.sunny_48px
        "Cloudy" -> R.drawable.cloud_48px
        "Rainy" -> R.drawable.rainy_48px
        else -> R.drawable.cloud_48px
    }

}
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview() {
    // Provide sample weather data for the preview
    val sampleWeatherData = WeatherData(temperature = 25, condition = "Cloudy")
    WeatherCard(currentWeather = sampleWeatherData)
}