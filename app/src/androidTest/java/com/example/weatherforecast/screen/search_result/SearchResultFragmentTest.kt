package com.example.weatherforecast.screen.search_result

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.ServiceLocator
import com.example.weatherforecast.atPosition
import com.example.weatherforecast.data.source.FakeAndroidForecastRepository
import com.example.weatherforecast.data.source.ForecastRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by viet on 1/28/21.
 */
@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SearchResultFragmentTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private lateinit var repository: ForecastRepository
    private lateinit var viewModel: SearchResultViewModel
    private lateinit var decorView: View
    private lateinit var loadingView: View

    @Before
    fun setUp() {
        repository = FakeAndroidForecastRepository()
        ServiceLocator.forecastRepository = repository
        viewModel = SearchResultViewModel(repository)

        activityRule.scenario.onActivity {
            decorView = it.window.decorView
            loadingView = it.findViewById(R.id.am_loading_container_fl)
        }
    }

    @After
    fun cleanUpDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    private fun launchFragment() {
        val navController = TestNavHostController(getApplicationContext()).apply {
            setGraph(R.navigation.nav_graph)
        }

        val scenario = launchFragmentInContainer(themeResId = R.style.AppTheme) {
            SearchResultFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }
    }

    @Test
    fun activeSearchResult_DisplayedInUi() {
        launchFragment()

        onView(withId(R.id.fsr_search_edt)).perform(
            replaceText("sa"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.fsr_search_btn)).perform(click())

        val expectedWarning = getApplicationContext<Context>().getString(
            R.string.invalid_search_length,
            SearchResultViewModel.MINIMUM_SEARCH_LENGTH
        )
        onView(withText(expectedWarning))
            .inRoot(withDecorView(not(decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun activeSearchResult_validInput_displaysListOfForecast() {
        launchFragment()

        assertThat(loadingView.visibility, `is`(View.GONE))

        onView(withId(R.id.fsr_search_edt))
            .perform(
                replaceText(FakeAndroidForecastRepository.VALID_INPUT),
                closeSoftKeyboard()
            )

        onView(withId(R.id.fsr_search_btn)).perform(click())

        checkViewHolderDisplayField(R.id.isr_date_title_tv)
        checkViewHolderDisplayField(R.id.isr_average_temp_title_tv)
        checkViewHolderDisplayField(R.id.isr_pressure_title_tv)
        checkViewHolderDisplayField(R.id.isr_humidity_title_tv)
        checkViewHolderDisplayField(R.id.isr_description_title_tv)
    }

    private fun checkViewHolderDisplayField(@IdRes id: Int) {
        onView(withId(R.id.fsr_result_list_rc))
            .check(matches(atPosition(0, hasDescendant(withId(id)))))
    }
}