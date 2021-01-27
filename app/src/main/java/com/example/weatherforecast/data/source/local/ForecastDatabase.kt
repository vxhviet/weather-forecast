package com.example.weatherforecast.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.constant.GlobalConstant
import com.example.weatherforecast.data.source.local.entity.CityEntity
import com.example.weatherforecast.data.source.local.entity.ForecastEntity
import net.sqlcipher.database.SupportFactory


/**
 * Created by viet on 1/21/21.
 */
@Database(
        entities = [
            CityEntity::class,
            ForecastEntity::class
        ],
        version = 1
)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun cityForecastDao(): CityForecastDao

    companion object {
        init {
            System.loadLibrary(GlobalConstant.NATIVE_LIB_NAME)
        }

        @JvmStatic
        private external fun getDBPass(): String

        private lateinit var INSTANCE: ForecastDatabase

        fun getDatabase(context: Context): ForecastDatabase {
            synchronized(ForecastDatabase::class.java) {
                if (!Companion::INSTANCE.isInitialized) {

                    val passphrase: ByteArray = getDBPass().toByteArray()
                    val factory = SupportFactory(passphrase)

                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ForecastDatabase::class.java,
                            "forecasts"
                    ).openHelperFactory(factory)
                            .build()
                }
            }
            return INSTANCE
        }
    }
}


