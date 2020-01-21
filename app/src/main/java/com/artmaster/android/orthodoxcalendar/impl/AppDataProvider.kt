package com.artmaster.android.orthodoxcalendar.impl

import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity

interface AppDataProvider {
    fun getDataSequence(start: Int, size: Int, year: Int): List<HolidayEntity>
    fun getData(year: Int): List<HolidayEntity>
    fun getMonthData(month: Int, year: Int): List<HolidayEntity>
    fun getMonthDays(month: Int, year: Int): List<Day>
    fun getDayData(day: Int, month: Int, year: Int): List<HolidayEntity>
}