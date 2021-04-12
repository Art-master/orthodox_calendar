package com.artmaster.android.orthodoxcalendar.impl

import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import java.util.*

interface AppDataProvider {
    fun getDataSequence(start: Int, size: Int, year: Int, filters: List<Filter>): List<Holiday>
    fun getData(year: Int, filters: List<Filter> = emptyList()): List<Holiday>
    fun getMonthData(month: Int, year: Int): List<Holiday>
    fun getMonthDays(month: Int, year: Int, filters: ArrayList<Filter> = ArrayList()): List<Day>
    fun getDayData(day: Int, month: Int, year: Int): List<Holiday>
    fun getHolidaysByTime(time: Time): List<Holiday>
}