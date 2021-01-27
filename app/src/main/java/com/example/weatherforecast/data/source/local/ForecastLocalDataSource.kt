package com.example.weatherforecast.data.source.local

import com.example.weatherforecast.data.source.ForecastDataSource
import com.example.weatherforecast.data.source.Result
import com.example.weatherforecast.data.source.local.entity.CityEntity
import com.example.weatherforecast.data.source.local.entity.ForecastEntity
import com.example.weatherforecast.data.source.local.entity.ForecastForCity
import com.example.weatherforecast.data.source.remote.model.ForecastResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by viet on 1/27/21.
 */
class ForecastLocalDataSource internal constructor(
        private val dao: CityForecastDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ForecastDataSource {

    override suspend fun saveCityToCache(city: CityEntity) = withContext(ioDispatcher) {
        dao.insertCity(city)
    }

    override suspend fun getCachedCityBasedOnInput(input: String): Result<CityEntity> = withContext(ioDispatcher) {
        return@withContext try {
            val result = dao.getCityBasedOnInput(input)
            if (result != null) {
                Result.Success(result)
            } else {
                Result.Error(Exception("City not found!"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getCachedForecastListForCity(cityID: Int): Result<ForecastForCity> = withContext(ioDispatcher) {
        return@withContext try {
            val result = dao.getForecastListForCity(cityID)
            if (result != null) {
                Result.Success(result)
            } else {
                Result.Error(Exception("Forecast List for City not found!"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun saveForecastListToCache(forecastList: List<ForecastEntity>) {
        dao.insertForecastList(forecastList)
    }

    override suspend fun deleteCachedForecastList(forecastList: List<ForecastEntity>) {
        dao.deleteForecastList(forecastList)
    }

    override suspend fun fetchRemoteDailyForecastForCity(input: String): Result<ForecastResponse> {
        // no op
        return Result.Error(Exception("Remote Daily Forecast for $input not found"))
    }
}