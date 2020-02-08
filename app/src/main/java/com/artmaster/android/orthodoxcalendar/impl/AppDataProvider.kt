package com.artmaster.android.orthodoxcalendar.impl

import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time

interface AppDataProvider {
    fun getDataSequence(start: Int, size: Int, year: Int): List<HolidayEntity>
    fun getData(year: Int): List<HolidayEntity>
    fun getMonthData(month: Int, year: Int): List<HolidayEntity>
    fun getMonthDays(month: Int, year: Int): List<Day>
    fun getDayData(day: Int, month: Int, year: Int): List<HolidayEntity>
    fun getHolidaysByTime(time: Time): List<HolidayEntity>
}