package com.example.weatherforecast.mapper

import com.example.weatherforecast.database.entity.ForecastEntity
import com.example.weatherforecast.database.entity.ForecastForCity
import com.example.weatherforecast.database.entity.Temperature
import com.example.weatherforecast.network.model.*

/**
 * Created by viet on 1/21/21.
 */
fun Forecast.toForecastEntity(cityID: Int) = ForecastEntity(
    cityID,
    dt,
    Temperature(temp?.min, temp?.max),
    pressure,
    humidity,
    weather?.firstOrNull()?.description
)

fun ForecastForCity.toForecastResponse() = ForecastResponse(
    list = this.forecastList.map {
        Forecast(
            dt = it.date,
            temp = Temp(max = it.temperature?.max, min = it.temperature?.min),
            pressure = it.pressure,
            humidity = it.humidity,
            weather = listOf(
                Weather(description = it.description)
            )
        )
    }
)
