package com.example.pr3_baranov

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName

data class WeatherResponse(val daily: Daily)

data class Daily(
    val time: List<String>,

    @SerializedName("temperature_2m_max")
    val maxTemp: List<Double>,

    @SerializedName("temperature_2m_min")
    val minTemp: List<Double>


)
interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") lat: Double = 49.9808,
        @Query("longitude") lon: Double = 36.2527,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min",
        @Query("forecast_days") days: Int = 1
    ): WeatherResponse
}

@Composable
fun WeatherTab() {
    var weatherInfo by remember { mutableStateOf("Завантаження даних...") }

    LaunchedEffect(Unit) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(WeatherApi::class.java)

            val response = withContext(Dispatchers.IO) { api.getForecast() }

            val date = response.daily.time.firstOrNull() ?: "Невідома дата"
            val maxTemp = response.daily.maxTemp.firstOrNull() ?: "N/A"
            val minTemp = response.daily.minTemp.firstOrNull() ?: "N/A"

            weatherInfo = "Погода в Харкові на $date:\nМакс. температура: $maxTemp°C\nМін. температура: $minTemp°C"
        } catch (e: Exception) {
            weatherInfo = "Помилка завантаження: ${e.message}"
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(text = weatherInfo, style = MaterialTheme.typography.titleLarge)
    }
}