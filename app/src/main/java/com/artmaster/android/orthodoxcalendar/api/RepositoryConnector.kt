package com.artmaster.android.orthodoxcalendar.api

import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time

interface RepositoryConnector {
    fun getData(year: Int, filters: Collection<Filter> = emptyList()): List<Holiday>
    fun getYearDays(year: Int, filters: Collection<Filter> = ArrayList()): List<Day>
    fun getMonthData(month: Int, year: Int): List<Holiday>
    fun getMonthDays(month: Int, year: Int, filters: Collection<Filter> = ArrayList()): List<Day>
    fun getDayData(day: Int, month: Int, year: Int): List<Holiday>
    fun getHolidaysByTime(time: Time): List<Holiday>
    fun insert(holiday: Holiday): Holiday
    fun insertHolidays(holidays: List<Holiday>)
    fun update(holiday: Holiday)
    fun getFullHolidayData(id: Long, year: Int): Holiday
    fun deleteById(id: Long)
    fun deleteCommonHolidays()
}