package com.example.weatherforecast.screen.search_result

import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.getOrAwaitValue
import org.hamcrest.Matchers.`is`
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

    @Before
    fun setupViewModel() {
        viewModel = SearchResultViewModel(ApplicationProvider.getApplicationContext())
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
        viewModel.onSearchButtonClicked(null)
        val result = viewModel.invalidSearchLengthEvent.getOrAwaitValue()
        assertThat(result, `is`(SearchResultViewModel.MINIMUM_SEARCH_LENGTH))
    }

    /*@Test
    fun testvalid() {
        val value = SpannableStringBuilder("saigon")
        viewModel.onSearchButtonClicked(value)
        val forecastResultList = viewModel.forecastResultListLiveData.getOrAwaitValue()
    }*/
}