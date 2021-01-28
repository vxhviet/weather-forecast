package com.example.weatherforecast

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.weatherforecast.constant.GlobalConstant
import com.example.weatherforecast.data.source.DefaultForecastRepository
import com.example.weatherforecast.data.source.ForecastDataSource
import com.example.weatherforecast.data.source.ForecastRepository
import com.example.weatherforecast.data.source.local.ForecastDatabase
import com.example.weatherforecast.data.source.local.ForecastLocalDataSource
import com.example.weatherforecast.data.source.remote.ForecastRemoteDataSource
import net.sqlcipher.database.SupportFactory

/**
 * Created by viet on 1/28/21.
 */
object ServiceLocator {
    init {
        try {
            System.loadLibrary(GlobalConstant.NATIVE_LIB_NAME)
        } catch (e: UnsatisfiedLinkError) {
            e.printStackTrace()
        }
    }

    private external fun getDBPass(): String

    private var database: ForecastDatabase? = null
    @Volatile
    var forecastRepository: ForecastRepository? = null
        @VisibleForTesting set

    fun provideForecastRepository(context: Context): ForecastRepository {
        synchronized(this) {
            return forecastRepository ?: createForecastRepository(context)
        }
    }

    private fun createForecastRepository(context: Context): ForecastRepository {
        val newRepo = DefaultForecastRepository(ForecastRemoteDataSource(), createForecastLocalDataSource(context))
        forecastRepository = newRepo
        return newRepo
    }

    private fun createForecastLocalDataSource(context: Context): ForecastDataSource {
        val database = database ?: createDataBase(context)
        return ForecastLocalDataSource(database.cityForecastDao())
    }

    private fun createDataBase(context: Context): ForecastDatabase {
        val passphrase: ByteArray = getDBPass().toByteArray()
        val factory = SupportFactory(passphrase)

        val result = Room.databaseBuilder(
            context.applicationContext,
            ForecastDatabase::class.java,
            "WeatherForecasts.db"
        )
            .openHelperFactory(factory)
            .build()

        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(this) {
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            forecastRepository = null
        }
    }
}