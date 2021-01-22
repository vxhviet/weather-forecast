package com.example.weatherforecast.util

import com.example.weatherforecast.constant.GlobalConstant.EMPTY
import com.example.weatherforecast.constant.Language
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by viet on 1/20/21.
 */
object DateUtil {
    private const val PATTERN_EEEE_dd_MMMM_yyyy = "EEEE, dd MMMM yyyy"
    private const val PATTERN_EEE_dd_MMM_yyyy = "EEE, dd MMM yyyy"

    fun parseTimestampToString(timestamp: Long?): String {
        return try {
            if (timestamp != null) {
                val date = getLocalDate(timestamp)

                val pattern =
                    getFormatPatternBasedOnCurrentLocale(PATTERN_EEEE_dd_MMMM_yyyy, PATTERN_EEE_dd_MMM_yyyy)
                date.format(DateTimeFormatter.ofPattern(pattern))
            } else {
                EMPTY
            }
        } catch (e: Exception) {
            e.printStackTrace()
            EMPTY
        }
    }

    private fun getLocalDate(timestamp: Long) = Instant.ofEpochSecond(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    private fun getFormatPatternBasedOnCurrentLocale(patternVI: String, patternEN: String): String {
        val currentLocale = getCurrentLocale()
        return when (currentLocale.language) {
            Language.VI.value -> patternVI
            else -> patternEN
        }
    }

    private fun getCurrentLocale() = Locale.getDefault()
}