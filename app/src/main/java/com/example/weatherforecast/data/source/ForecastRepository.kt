package com.example.weatherforecast.data.source

import com.example.weatherforecast.data.source.remote.model.ForecastResponse

/**
 * Created by viet on 1/28/21.
 */
interface ForecastRepository {
    suspend fun getDailyForecastForCity(input: String): Result<ForecastResponse>
}