package com.example.weatherforecast.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by viet on 1/21/21.
 */
data class ForecastForCity(
    @Embedded val city: CityEntity,
    @Relation(
        parentColumn = "cityID",
        entityColumn = "forecastCityID"
    )
    val forecastList: List<ForecastEntity>
)