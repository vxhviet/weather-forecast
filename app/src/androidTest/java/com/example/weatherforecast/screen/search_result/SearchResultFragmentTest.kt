package com.example.weatherforecast.screen.search_result

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherforecast.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by viet on 1/28/21.
 */
@MediumTest
@RunWith(AndroidJUnit4::class)
class SearchResultFragmentTest {
    @Test
    fun activeSearchResult_DisplayedInUi() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
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

}