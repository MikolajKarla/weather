package com.example.myapplication3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.*
import java.io.IOException

// Main activity class
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Sets the UI content for activity, defining the app's layout
        setContent {
            WeatherApp() // Calls the main WeatherApp composable function to build the UI
        }
    }
}

// A composable function to display the weather data
@Composable
fun WeatherApp() {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var temperature by remember { mutableStateOf("Loading...") }
    var humidity by remember { mutableStateOf("Loading...") }

    // LaunchedEffect to fetch weather data
    LaunchedEffect(Unit) {
        val weather = fetchWeatherData()
        temperature = weather.temperature
        humidity = weather.humidity
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Text(text = "Temperature: $temperature")
        Text(text = "Humidity: $humidity")
        Button(onClick = { /* Handle button click */ }) {
            Text(text = "Submit")
        }
    }
}

data class Weather(val temperature: String, val humidity: String)

suspend fun fetchWeatherData(): Weather = withContext(Dispatchers.IO) {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://api.tomorrow.io/v4/weather/realtime?location=warsaw&apikey=c3Vm1eK7lkW1uMSBtbqHeVsTTXdrjjFg")
        .get()
        .addHeader("accept", "application/json")
        .build()

    try {
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val jsonString = response.body()?.string()
            val temperature = parseTemperature(jsonString)
            val humidity = parseHumidity(jsonString)
            Weather(temperature, humidity)
        } else {
            Weather("Error", "Error")
        }
    } catch (e: IOException) {
        Weather("Error", "Error")
    }
}

fun parseTemperature(jsonString: String?): String {
    // Parse temperature from JSON string
    return jsonString.toString();
}

fun parseHumidity(jsonString: String?): String {
    // Parse humidity from JSON string
    return jsonString.toString();

}