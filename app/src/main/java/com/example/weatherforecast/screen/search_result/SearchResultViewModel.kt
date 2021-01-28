package com.example.weatherforecast.screen.search_result

import android.text.Editable
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.util.livedata.Event
import com.example.weatherforecast.util.livedata.VoidEvent
import com.example.weatherforecast.base.BaseViewModel
import com.example.weatherforecast.data.source.ForecastRepository
import com.example.weatherforecast.data.source.Result
import com.example.weatherforecast.data.source.remote.model.Temp
import kotlinx.coroutines.launch

/**
 * Created by viet on 1/19/21.
 */
@Suppress("UNCHECKED_CAST")
class SearchResultViewModelFactory (
        private val repository: ForecastRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            (SearchResultViewModel(repository) as T)
}

class SearchResultViewModel(private val repository: ForecastRepository) : BaseViewModel() {
    companion object {
        @VisibleForTesting
        internal const val MINIMUM_SEARCH_LENGTH = 3
    }

    private val _forecastResultListLiveData: MutableLiveData<List<SearchResultAdapter.ForecastData>> by lazy {
        MutableLiveData<List<SearchResultAdapter.ForecastData>>()
    }
    val forecastResultListLiveData: LiveData<List<SearchResultAdapter.ForecastData>> = _forecastResultListLiveData

    private val _invalidSearchLengthEvent: MutableLiveData<Event<Int>> by lazy { MutableLiveData<Event<Int>>() }
    val invalidSearchLengthEvent: LiveData<Event<Int>> = _invalidSearchLengthEvent

    private val _dismissKeyboardEvent: MutableLiveData<VoidEvent> by lazy { MutableLiveData<VoidEvent>() }
    val dismissKeyboardEvent: LiveData<VoidEvent> = _dismissKeyboardEvent

    fun onSearchButtonClicked(text: Editable?) {
        if (text == null || text.length < MINIMUM_SEARCH_LENGTH) {
            _invalidSearchLengthEvent.value = Event(MINIMUM_SEARCH_LENGTH)
        } else {
            handleDailyForecastForCity(text.toString().trim())
        }
    }

    private fun handleDailyForecastForCity(input: String) = viewModelScope.launch {
        isLoadingEvent.value = Event(true)
        when (val forecastResponse = repository.getDailyForecastForCity(input)) {
            is Result.Success -> {
                val result = mutableListOf<SearchResultAdapter.ForecastData>()
                forecastResponse.data.list?.forEach { foreCast ->
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
                    _dismissKeyboardEvent.value = VoidEvent()
                }
            }
            is Result.Error -> {
                errorLiveData.value = forecastResponse
            }
        }
        isLoadingEvent.value = Event(false)
    }

    private fun getAverageTemperature(temperature: Temp?) = if (temperature?.max != null && temperature.min != null) {
        (temperature.max + temperature.min) / 2
    } else {
        null
    }
}