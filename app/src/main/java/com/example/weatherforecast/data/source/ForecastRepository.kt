package com.example.weatherforecast.data.source

import android.app.Application
import androidx.room.Room
import com.example.weatherforecast.constant.GlobalConstant
import com.example.weatherforecast.data.source.local.ForecastDatabase
import com.example.weatherforecast.data.source.local.entity.CityEntity
import com.example.weatherforecast.data.source.local.entity.ForecastEntity
import com.example.weatherforecast.data.mapper.toForecastEntity
import com.example.weatherforecast.data.mapper.toForecastResponse
import com.example.weatherforecast.data.source.local.ForecastLocalDataSource
import com.example.weatherforecast.data.source.local.entity.ForecastForCity
import com.example.weatherforecast.data.source.remote.ForecastRemoteDataSource
import com.example.weatherforecast.data.source.remote.model.ForecastResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.sqlcipher.database.SupportFactory
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Created by viet on 1/21/21.
 */
class ForecastRepository(
        private val remoteDataSource: ForecastDataSource,
        private val localDataSource: ForecastDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    companion object {
        init {
            try {
                System.loadLibrary(GlobalConstant.NATIVE_LIB_NAME)
            } catch (e: UnsatisfiedLinkError) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        private external fun getDBPass(): String

        private const val REFRESH_THRESHOLD_IN_DAYS = 1

        @Volatile
        private var INSTANCE: ForecastRepository? = null

        fun getRepository(app: Application): ForecastRepository {
            return INSTANCE ?: synchronized(this) {
                val databaseDao = initDataBase(app).cityForecastDao()

                ForecastRepository(
                        ForecastRemoteDataSource(),
                        ForecastLocalDataSource(databaseDao)
                ).also {
                    INSTANCE = it
                }
            }
        }

        private fun initDataBase(app: Application): ForecastDatabase {
            val passphrase: ByteArray = getDBPass().toByteArray()
            val factory = SupportFactory(passphrase)

            return Room.databaseBuilder(
                    app,
                    ForecastDatabase::class.java,
                    "forecasts"
            )
                    .openHelperFactory(factory)
                    .build()
        }
    }

    suspend fun getDailyForecastForCity(input: String): Result<ForecastResponse> {
        return when (val cachedCityResult = localDataSource.getCachedCityBasedOnInput(input)) {
            is Result.Error -> fetchAndCacheRemoteDailyForecastForCity(input)
            is Result.Success<CityEntity> -> {
                val cachedCity = cachedCityResult.data
                if (isCachedCityStillFresh(cachedCity)) {
                    when (val cachedForecastListResult = localDataSource.getCachedForecastListForCity(cachedCity.cityID)) {
                        is Result.Error -> cachedForecastListResult
                        is Result.Success<ForecastForCity> -> Result.Success(cachedForecastListResult.data.toForecastResponse())
                    }
                } else {
                    fetchAndCacheRemoteDailyForecastForCity(input)
                }
            }
        }
    }

    private suspend fun fetchAndCacheRemoteDailyForecastForCity(input: String): Result<ForecastResponse> {
        val result = remoteDataSource.fetchRemoteDailyForecastForCity(input)

        if (result is Result.Success) {
            saveForecastResponse(result.data, input)
        }

        return result
    }

    private suspend fun saveForecastResponse(response: ForecastResponse?, searchInput: String) {
        val cityID = response?.city?.id
        val cityName = response?.city?.name
        if (cityID != null && cityName != null) {
            val cityEntity = CityEntity(cityID, response.city.name, searchInput, Instant.now().epochSecond)
            localDataSource.saveCityToCache(cityEntity)
        }

        response?.city?.id?.let { cityID ->
            // remove old forecast list
            val cachedForecastListResult = localDataSource.getCachedForecastListForCity(cityID)
            if (cachedForecastListResult is Result.Success) {
                localDataSource.deleteCachedForecastList(cachedForecastListResult.data.forecastList)
            }

            // save new forecast list
            val forecastList = mutableListOf<ForecastEntity>()
            response.list?.forEach {
                forecastList.add(it.toForecastEntity(cityID))
            }
            localDataSource.saveForecastListToCache(forecastList)
        }
    }

    private fun isCachedCityStillFresh(city: CityEntity): Boolean {
        val lastAccessed = Instant.ofEpochSecond(city.lastUpdate)
        val current = Instant.now()
        return ChronoUnit.DAYS.between(lastAccessed, current) < REFRESH_THRESHOLD_IN_DAYS
    }
}

