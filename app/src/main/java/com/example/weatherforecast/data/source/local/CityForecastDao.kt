package com.example.weatherforecast.data.source.local

import androidx.room.*
import com.example.weatherforecast.data.source.local.entity.CityEntity
import com.example.weatherforecast.data.source.local.entity.ForecastEntity
import com.example.weatherforecast.data.source.local.entity.ForecastForCity

/**
 * Created by viet on 1/21/21.
 */
@Dao
interface CityForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Delete
    suspend fun deleteCity(city: CityEntity)

    @Query("SELECT * FROM CityEntity WHERE searchInput = :input")
    suspend fun getCityBasedOnInput(input: String): CityEntity?

    @Transaction
    @Query("SELECT * FROM CityEntity WHERE cityID = :cityID")
    fun getForecastListForCity(cityID: Int): ForecastForCity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastList(forecastList: List<ForecastEntity>)

    @Delete
    suspend fun deleteForecastList(forecastList: List<ForecastEntity>)
}