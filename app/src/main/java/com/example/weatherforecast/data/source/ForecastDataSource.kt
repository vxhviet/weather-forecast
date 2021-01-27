package com.example.weatherforecast.data.source

import com.example.weatherforecast.data.source.local.entity.CityEntity
import com.example.weatherforecast.data.source.local.entity.ForecastEntity
import com.example.weatherforecast.data.source.local.entity.ForecastForCity
import com.example.weatherforecast.data.source.remote.model.ForecastResponse

/**
 * Created by viet on 1/27/21.
 */
interface ForecastDataSource {
    suspend fun saveCityToCache(city: CityEntity)
    suspend fun getCachedCityBasedOnInput(input: String): Result<CityEntity>
    suspend fun getCachedForecastListForCity(cityID: Int): Result<ForecastForCity>
    suspend fun saveForecastListToCache(forecastList: List<ForecastEntity>)
    suspend fun deleteCachedForecastList(forecastList: List<ForecastEntity>)
    suspend fun fetchRemoteDailyForecastForCity(input: String): Result<ForecastResponse>
}