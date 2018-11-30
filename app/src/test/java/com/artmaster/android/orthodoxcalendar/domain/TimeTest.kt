package com.artmaster.android.orthodoxcalendar.domain

import org.junit.Assert
import org.junit.Test

import java.util.Calendar

class TimeTest {

    @Test
    fun calculateDateTest() {
        val year = 2018
        val month = 11
        val day = 1
        val hour = 10
        var calendar = Time.calculateDate(year, month, day, Calendar.DAY_OF_MONTH, 3)
        Assert.assertEquals(calendar.get(Calendar.DAY_OF_MONTH).toLong(), 4)

        calendar = Time.calculateDate(year, month, day, hour, Calendar.HOUR_OF_DAY, 3)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY).toLong(), 13)

        calendar = Time.calculateDate(year, month, day, hour, Calendar.HOUR_OF_DAY, -20)
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY).toLong(), 14)
    }
}
