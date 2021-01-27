package com.example.weatherforecast.data.source.remote

import com.example.weatherforecast.BuildConfig
import com.example.weatherforecast.data.source.remote.model.ForecastResponse
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Created by viet on 1/19/21.
 */
interface ApiService {
    companion object {
        private val HOST_URL = "https://api.openweathermap.org/data/2.5/"
    }

    object Client {
        val instance: ApiService by lazy {
            val baseClient = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)

            val httpClient = if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                baseClient.addInterceptor(loggingInterceptor).build()
            } else {
                baseClient.build()
            }

            val gson = GsonBuilder().create()

            Retrofit.Builder()
                    .baseUrl(HOST_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(httpClient)
                    .build()
                    .create(ApiService::class.java)
        }
    }

    @GET("forecast/daily")
    suspend fun getDailyForecastForCity(
        @Query("q") cityName: String,
        @Query("cnt") numberOfForecastDays: Int,
        @Query("appid") appID: String,
        @Query("units") unit: String?
    ): Response<ForecastResponse>
}