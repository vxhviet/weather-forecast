package com.example.weatherforecast.screen.search_result

import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.data.source.FakeForecastRepository
import com.example.weatherforecast.getOrAwaitValue
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by viet on 1/26/21.
 */
@RunWith(AndroidJUnit4::class)
class SearchResultViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchResultViewModel
    private lateinit var repository: FakeForecastRepository

    @Before
    fun setupViewModel() {
        repository = FakeForecastRepository()
        viewModel = SearchResultViewModel(repository)
    }

    @Test
    fun onSearchButtonClicked_nullInput_invalidSearchLengthEventCalled() {
        onSearchButtonClicked_invalidInput_invalidSearchLengthEventCalled(null)
    }

    @Test
    fun onSearchButtonClicked_lessThanMinimumSearchLength_invalidSearchLengthEventCalled() {
        val value = SpannableStringBuilder("sa")
        onSearchButtonClicked_invalidInput_invalidSearchLengthEventCalled(value)
    }

    private fun onSearchButtonClicked_invalidInput_invalidSearchLengthEventCalled(input: Editable?) {
        viewModel.onSearchButtonClicked(input)
        val result = viewModel.invalidSearchLengthEvent.getOrAwaitValue()
        assertThat(result.getContentIfNotHandled(), `is`(SearchResultViewModel.MINIMUM_SEARCH_LENGTH))
    }

    @Test
    fun onSearchButtonClicked_validInput_returnsForecastResultAndDismissKeyboard() {
        val value = SpannableStringBuilder(FakeForecastRepository.VALID_INPUT)
        viewModel.onSearchButtonClicked(value)
        val forecastResult = viewModel.forecastResultListLiveData.getOrAwaitValue()
        assertThat(forecastResult, `is`(not(empty())))

        val dismissKeyboardEvent = viewModel.dismissKeyboardEvent.getOrAwaitValue()
        assertThat(dismissKeyboardEvent, not(nullValue()))
    }

    @Test
    fun onSearchButtonClicked_invalidInput_displayError() {
        val value = SpannableStringBuilder(FakeForecastRepository.INVALID_INPUT)
        viewModel.onSearchButtonClicked(value)
        val error = viewModel.errorLiveData.getOrAwaitValue()
        assertThat(error, not(nullValue()))
    }
}