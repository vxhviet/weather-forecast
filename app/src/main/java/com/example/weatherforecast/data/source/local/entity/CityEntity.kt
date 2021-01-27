package com.example.weatherforecast.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by viet on 1/21/21.
 */
@Entity
data class CityEntity(
    @PrimaryKey
    val cityID: Int,
    val name: String,
    val searchInput: String,
    val lastUpdate: Long
)