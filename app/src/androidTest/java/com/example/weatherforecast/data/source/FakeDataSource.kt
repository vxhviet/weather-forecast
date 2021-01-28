package com.example.weatherforecast.data.source

import com.example.weatherforecast.data.source.local.entity.CityEntity
import com.example.weatherforecast.data.source.local.entity.ForecastEntity
import com.example.weatherforecast.data.source.local.entity.ForecastForCity
import com.example.weatherforecast.data.source.remote.model.ForecastResponse

/**
 * Created by viet on 1/27/21.
 */
class FakeDataSource(private val localCityList: MutableList<CityEntity>? = null,
                     private val localForecastList: List<ForecastForCity>? = null,
                     private val remoteForecastMap: Map<String, ForecastResponse>? = null) : ForecastDataSource {
    override suspend fun saveCityToCache(city: CityEntity) {
        // no op
    }

    override suspend fun getCachedCityBasedOnInput(input: String): Result<CityEntity> {
        val cachedCity = localCityList?.find { it.searchInput.equals(input, true) }

        return if (cachedCity != null) {
            Result.Success(cachedCity)
        } else {
            Result.Error(Exception("No cached city for $input"))
        }
    }

    override suspend fun getCachedForecastListForCity(cityID: Int): Result<ForecastForCity> {
        val cachedForecast = localForecastList?.find { it.city.cityID == cityID }
        return if (cachedForecast != null) {
            Result.Success(cachedForecast)
        } else {
            Result.Error(Exception("No cached forecast for $cityID"))
        }
    }

    override suspend fun saveForecastListToCache(forecastList: List<ForecastEntity>) {
        // no op
    }

    override suspend fun deleteCachedForecastList(forecastList: List<ForecastEntity>) {
        // no op
    }

    override suspend fun fetchRemoteDailyForecastForCity(input: String): Result<ForecastResponse> {
        val remoteForecast = remoteForecastMap?.get(input)

        return if (remoteForecast != null) {
            Result.Success(remoteForecast)
        } else {
            Result.Error(Exception("No remote forecast for $input"))
        }
    }
}