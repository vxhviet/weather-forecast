package com.example.weatherforecast.data.source

import com.example.weatherforecast.data.source.remote.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Created by viet on 1/28/21.
 */
class FakeAndroidForecastRepository : ForecastRepository {
    companion object {
        const val VALID_INPUT = "Sydney"
        const val FAKE_NETWORK_DELAY_IN_MILIS = 2000L
    }

    private val forecastResponse = ForecastResponse(
        City(id = 1, name = "Sydney"),
        list = listOf(
            Forecast(
                dt = Instant.now().epochSecond,
                temp = Temp(12f, 22f),
                pressure = 25,
                humidity = 37,
                weather = listOf(Weather(description = "stormy"))
            ),
            Forecast(
                dt = Instant.now().plus(1, ChronoUnit.DAYS).epochSecond,
                temp = Temp(17f, 29f),
                pressure = 28,
                humidity = 32,
                weather = listOf(Weather(description = "sunny"))
            )
        )
    )

    override suspend fun getDailyForecastForCity(input: String): Result<ForecastResponse> {
        return if (input == VALID_INPUT) {
            Result.Success(forecastResponse)
        } else {
            Result.Error(Exception("City not found"))
        }
    }
}