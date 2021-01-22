package com.example.weatherforecast.repository

import com.example.weatherforecast.base.BaseRepository
import com.example.weatherforecast.constant.GlobalConstant
import com.example.weatherforecast.database.ForecastDatabase
import com.example.weatherforecast.database.entity.CityEntity
import com.example.weatherforecast.database.entity.ForecastEntity
import com.example.weatherforecast.mapper.toForecastEntity
import com.example.weatherforecast.mapper.toForecastResponse
import com.example.weatherforecast.network.model.ForecastResponse
import com.example.weatherforecast.network.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Created by viet on 1/21/21.
 */
class ForecastRepository(private val database: ForecastDatabase) : BaseRepository() {
    companion object {
        private const val REFRESH_THRESHOLD = 1
    }

    init {
        System.loadLibrary(GlobalConstant.NATIVE_LIB_NAME)
    }

    private external fun getAppID(): String

    suspend fun getDailyForecastForCity(input: String): ForecastResponse? {
        var result: ForecastResponse?

        withContext(Dispatchers.IO) {
            val cachedCity = database.cityForecastDao().getCityBasedOnInput(input)

            result = if (cachedCity == null) {
                fetchRemoteDailyForecastForCity(input)
            } else {
                if (isCachedCityStillFresh(cachedCity)) {
                    val cachedForecastList =
                        database.cityForecastDao().getForecastListForCity(cachedCity.cityID)
                    cachedForecastList.toForecastResponse()
                } else {
                    fetchRemoteDailyForecastForCity(input)
                }
            }
        }

        return result
    }

    private suspend fun fetchRemoteDailyForecastForCity(input: String): ForecastResponse? {
        var result: ForecastResponse? = null

        withContext(Dispatchers.IO) {
            isLoadingEvent.postValue(true)
            var response: Response<ForecastResponse>? = null

            try {
                response = apiService.getDailyForecastForCity(
                    input,
                    10,
                    getAppID(),
                    "Metric"
                )
            } catch (e: Exception) {
                e.printStackTrace()
                errorLiveData.postValue(ErrorResponse(e))
            }

            isLoadingEvent.postValue(false)
            response?.let {
                if (it.isSuccessful) {
                    result = it.body()
                    saveForecastResponse(result, input)
                } else {
                    errorLiveData.postValue(ErrorResponse(it))
                }
            }
        }

        return result
    }

    private suspend fun saveForecastResponse(response: ForecastResponse?, searchInput: String) =
        withContext(Dispatchers.IO) {
            val cityID = response?.city?.id
            val cityName = response?.city?.name
            if (cityID != null && cityName != null) {
                val cityEntity = CityEntity(cityID, response.city.name, searchInput, Instant.now().epochSecond)
                database.cityForecastDao().insertCity(cityEntity)
            }

            response?.city?.id?.let { cityID ->
                // remove old forecast list
                val cachedForecastList = database.cityForecastDao().getForecastListForCity(cityID).forecastList
                database.cityForecastDao().deleteForecastList(cachedForecastList)

                // save new forecast list
                val forecastList = mutableListOf<ForecastEntity>()
                response.list?.forEach {
                    forecastList.add(it.toForecastEntity(cityID))
                }
                database.cityForecastDao().insertForecastList(forecastList)
            }
        }

    private fun isCachedCityStillFresh(city: CityEntity): Boolean {
        val lastAccessed = Instant.ofEpochSecond(city.lastUpdate)
        val current = Instant.now()
        return ChronoUnit.DAYS.between(lastAccessed, current) < REFRESH_THRESHOLD
    }
}