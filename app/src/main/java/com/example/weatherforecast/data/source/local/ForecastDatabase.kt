package com.example.weatherforecast.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherforecast.data.source.local.entity.CityEntity
import com.example.weatherforecast.data.source.local.entity.ForecastEntity


/**
 * Created by viet on 1/21/21.
 */
@Database(
        entities = [
            CityEntity::class,
            ForecastEntity::class
        ],
        version = 1
)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun cityForecastDao(): CityForecastDao
}


