package com.example.weatherforecast.data.source

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.data.mapper.toForecastResponse
import com.example.weatherforecast.data.source.local.entity.CityEntity
import com.example.weatherforecast.data.source.local.entity.ForecastEntity
import com.example.weatherforecast.data.source.local.entity.ForecastForCity
import com.example.weatherforecast.data.source.local.entity.Temperature
import com.example.weatherforecast.data.source.remote.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Created by viet on 1/27/21.
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class ForecastRepositoryTest {
    private val city1 = CityEntity(1, "Ho Chi Minh City", "saigon", Instant.now().epochSecond)
    private val city2 = CityEntity(2, "Sydney", "sydney", Instant.now().minus(2, ChronoUnit.DAYS).epochSecond)
    private val localCityList = listOf(city1, city2)

    private val localForecastForCity1 = ForecastForCity(
            city1,
            listOf(
                    ForecastEntity(city1.cityID,
                            Instant.now().plus(1, ChronoUnit.DAYS).epochSecond,
                            Temperature(10f, 20f),
                            10,
                            10,
                            "windy"),
                    ForecastEntity(city1.cityID,
                            Instant.now().plus(2, ChronoUnit.DAYS).epochSecond,
                            Temperature(20f, 30f),
                            20,
                            20,
                            "rainy")
            )
    )
    private val localForecastForCity2 = ForecastForCity(
            city2,
            listOf(
                    ForecastEntity(city2.cityID,
                            Instant.now().plus(1, ChronoUnit.DAYS).epochSecond,
                            Temperature(10f, 20f),
                            10,
                            10,
                            "windy"),
                    ForecastEntity(city2.cityID,
                            Instant.now().plus(2, ChronoUnit.DAYS).epochSecond,
                            Temperature(20f, 30f),
                            20,
                            20,
                            "rainy")
            )
    )
    private val localForecastList = listOf(localForecastForCity1, localForecastForCity2)

    private val remoteForecastForCity2 = ForecastResponse(
            City(id = city2.cityID, name = city2.name),
            list = listOf(
                    Forecast(
                            dt = Instant.now().epochSecond,
                            temp = Temp(12f, 22f),
                            pressure = 25,
                            humidity = 37,
                            weather = listOf(Weather(description = "stormy"))
                    ),
                    Forecast(
                            dt = Instant.now().plus(1, ChronoUnit.DAYS).epochSecond,
                            temp = Temp(17f, 29f),
                            pressure = 28,
                            humidity = 32,
                            weather = listOf(Weather(description = "sunny"))
                    )
            )
    )
    private val searchPhrase3 = "melbourne"
    private val remoteForecastForCity3 = ForecastResponse(
            City(id = 3, name = "Melbourne"),
            list = listOf(
                    Forecast(
                            dt = Instant.now().epochSecond,
                            temp = Temp(0f, 9f),
                            pressure = 32,
                            humidity = 12,
                            weather = listOf(Weather(description = "very cold"))
                    ),
                    Forecast(
                            dt = Instant.now().plus(1, ChronoUnit.DAYS).epochSecond,
                            temp = Temp(0f, 1f),
                            pressure = 15,
                            humidity = 37,
                            weather = listOf(Weather(description = "freezing"))
                    )
            )
    )
    private val remoteForecastMap = mapOf(
            city2.searchInput to remoteForecastForCity2,
            searchPhrase3 to remoteForecastForCity3
    )

    private lateinit var localDataSource: FakeDataSource
    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var forecastRepository: ForecastRepository

    @Before
    fun createRepository() {
        remoteDataSource = FakeDataSource(remoteForecastMap = remoteForecastMap)
        localDataSource = FakeDataSource(localCityList.toMutableList(), localForecastList)
        forecastRepository = ForecastRepository(
                remoteDataSource, localDataSource, Dispatchers.Unconfined
        )
    }

    @Test
    fun getDailyForecastForCity_validInputCachedSearch_returnsCachedForecastFromLocalDataSource() = runBlockingTest {
        val result = forecastRepository.getDailyForecastForCity(city1.searchInput) as? Result.Success
        assertThat(result?.data, IsEqual(localForecastForCity1.toForecastResponse()))
    }

    @Test
    fun getDailyForecastForCity_validInputOutdatedData_returnsForecastFromRemoteDataSource() = runBlockingTest {
        val result = forecastRepository.getDailyForecastForCity(city2.searchInput) as? Result.Success
        assertThat(result?.data, IsEqual(remoteForecastForCity2))
    }

    @Test
    fun getDailyForecastForCity_validInputFreshData_returnsForecastFromRemoteDataSource() = runBlockingTest {
        val result = forecastRepository.getDailyForecastForCity(searchPhrase3) as? Result.Success
        assertThat(result?.data, IsEqual(remoteForecastForCity3))
    }
}