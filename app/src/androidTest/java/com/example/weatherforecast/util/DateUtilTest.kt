package com.example.weatherforecast.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


/**
 * Created by viet on 1/26/21.
 */
@RunWith(AndroidJUnit4::class)
class DateUtilTest {
    @Test
    fun parseTimestampToString_EN_returnsTue26Jan2021() {
        val locale = Locale("en", "EN")
        val result = parseTimestampToStringBase(locale)
        assertThat(result, Matchers.`is`("Tue, 26 Jan 2021"))
    }

    @Test
    fun parseTimestampToString_VI_returnsThuBa26Thang12021() {
        val locale = Locale("vi", "VI")
        val result = parseTimestampToStringBase(locale)
        assertThat(result, Matchers.`is`("Thứ Ba, 26 tháng 1 2021"))
    }

    private fun parseTimestampToStringBase(locale: Locale): String {
        Locale.setDefault(locale)
        val timestamp = 1611645503L
        return DateUtil.parseTimestampToString(timestamp)
    }
}