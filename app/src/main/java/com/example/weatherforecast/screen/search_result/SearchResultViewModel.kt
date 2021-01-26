package com.example.weatherforecast.screen.search_result

import android.app.Application
import android.text.Editable
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.base.BaseViewModel
import com.example.weatherforecast.database.ForecastDatabase
import com.example.weatherforecast.network.model.Temp
import com.example.weatherforecast.network.SingleLiveEvent
import com.example.weatherforecast.repository.ForecastRepository
import kotlinx.coroutines.*

/**
 * Created by viet on 1/19/21.
 */
class SearchResultViewModel(application: Application) : BaseViewModel(application) {
    companion object {
        @VisibleForTesting
        internal const val MINIMUM_SEARCH_LENGTH = 3
    }

    override val repository: ForecastRepository by lazy { ForecastRepository(ForecastDatabase.getDatabase(application)) }

    private val _forecastResultListLiveData: MutableLiveData<List<SearchResultAdapter.ForecastData>> by lazy {
        MutableLiveData<List<SearchResultAdapter.ForecastData>>()
    }
    val forecastResultListLiveData: LiveData<List<SearchResultAdapter.ForecastData>> = _forecastResultListLiveData

    private val _invalidSearchLengthEvent: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>() }
    val invalidSearchLengthEvent: LiveData<Int> = _invalidSearchLengthEvent

    private val _dismissKeyboardEvent: SingleLiveEvent<Void> by lazy { SingleLiveEvent<Void>() }
    val dismissKeyboardEvent: LiveData<Void> = _dismissKeyboardEvent

    fun onSearchButtonClicked(text: Editable?) {
        if (text == null || text.length < MINIMUM_SEARCH_LENGTH) {
            _invalidSearchLengthEvent.value = MINIMUM_SEARCH_LENGTH
        } else {
            handleDailyForecastForCity(text.toString().trim())
        }
    }

    private fun handleDailyForecastForCity(input: String) = viewModelScope.launch {
        val result = mutableListOf<SearchResultAdapter.ForecastData>()
        val forecastResponse = repository.getDailyForecastForCity(input)

        forecastResponse?.list?.forEach { foreCast ->
            val averageTemperature = getAverageTemperature(foreCast.temp)
            result.add(
                SearchResultAdapter.ForecastData(
                    foreCast.dt,
                    averageTemperature,
                    foreCast.pressure,
                    foreCast.humidity,
                    foreCast.weather?.firstOrNull()?.description
                )
            )
        }

        _forecastResultListLiveData.value = result
        if (result.isNotEmpty()) {
            _dismissKeyboardEvent.call()
        }
    }

    private fun getAverageTemperature(temperature: Temp?) = if (temperature?.max != null && temperature.min != null) {
        (temperature.max + temperature.min) / 2
    } else {
        null
    }
}