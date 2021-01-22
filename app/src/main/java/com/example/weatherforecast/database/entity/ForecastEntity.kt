package com.example.weatherforecast.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by viet on 1/21/21.
 */
@Entity
data class ForecastEntity(
    val forecastCityID: Int?,
    val date: Long?,
    @Embedded
    val temperature: Temperature?,
    val pressure: Int?,
    val humidity: Int?,
    val description: String?
) {
    @PrimaryKey(autoGenerate = true)
    var forecastID: Int = 0
}