package com.example.weatherforecast

import android.app.Application
import com.example.weatherforecast.logger.ReleaseTree
import com.github.ybq.android.spinkit.BuildConfig
import timber.log.Timber

/**
 * Created by viet on 1/20/21.
 */
class WeatherForecastApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}