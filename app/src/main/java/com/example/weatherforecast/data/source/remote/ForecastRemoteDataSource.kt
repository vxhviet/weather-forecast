package com.example.weatherforecast.data.source.remote

import com.example.weatherforecast.constant.GlobalConstant
import com.example.weatherforecast.data.source.ForecastDataSource
import com.example.weatherforecast.data.source.Result
import com.example.weatherforecast.data.source.local.entity.CityEntity
import com.example.weatherforecast.data.source.local.entity.ForecastEntity
import com.example.weatherforecast.data.source.local.entity.ForecastForCity
import com.example.weatherforecast.data.source.remote.model.ForecastResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

/**
 * Created by viet on 1/27/21.
 */
class ForecastRemoteDataSource internal constructor(
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ForecastDataSource {

    private  val apiService = ApiService.Client.instance

    init {
        try {
            System.loadLibrary(GlobalConstant.NATIVE_LIB_NAME)
        } catch (e: UnsatisfiedLinkError) {
            e.printStackTrace()
        }
    }

    private external fun getAppID(): String

    override suspend fun saveCityToCache(city: CityEntity) {
        // no op
    }

    override suspend fun getCachedCityBasedOnInput(input: String): Result<CityEntity> {
        // no op
        return Result.Error(Exception("No Cached City"))
    }

    override suspend fun getCachedForecastListForCity(cityID: Int): Result<ForecastForCity> {
        // no op
        return Result.Error(Exception("No Cached Forecast for City"))
    }

    override suspend fun saveForecastListToCache(forecastList: List<ForecastEntity>) {
        // no op
    }

    override suspend fun deleteCachedForecastList(forecastList: List<ForecastEntity>) {
        // no op
    }

    override suspend fun fetchRemoteDailyForecastForCity(input: String): Result<ForecastResponse> = withContext(ioDispatcher) {
        return@withContext try {
            val result = apiService.getDailyForecastForCity(
                    input,
                    10,
                    getAppID(),
                    "Metric"
            )

            val body = result.body()
            if (result.isSuccessful && body != null) {
                Result.Success(body)
            } else {
                parseErrorBody(result)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun parseErrorBody(response: Response<ForecastResponse>) : Result.Error {
        var errorMessage = ""
        response.errorBody()?.string()?.let {
            val jsonObj = JSONObject(it)
            errorMessage = jsonObj.getString("message")
        }

        return Result.Error(Exception(errorMessage))
    }
}