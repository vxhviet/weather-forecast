package com.example.weatherforecast.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.database.dao.CityForecastDao
import com.example.weatherforecast.database.entity.CityEntity
import com.example.weatherforecast.database.entity.ForecastEntity

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

private lateinit var INSTANCE: ForecastDatabase

fun getDatabase(context: Context): ForecastDatabase {
    synchronized(ForecastDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                ForecastDatabase::class.java,
                "forecasts"
            ).build()
        }
    }
    return INSTANCE
}