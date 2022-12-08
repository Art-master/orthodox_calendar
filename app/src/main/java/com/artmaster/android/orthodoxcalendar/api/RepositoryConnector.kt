package com.artmaster.android.orthodoxcalendar.api

import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time

interface RepositoryConnector {
    suspend fun getData(year: Int, filters: Collection<Filter> = emptyList()): List<Holiday>
    suspend fun getYearDays(year: Int, filters: Collection<Filter> = ArrayList()): List<Day>
    suspend fun getMonthData(month: Int, year: Int): List<Holiday>
    fun getMonthDays(month: Int, year: Int, filters: Collection<Filter> = ArrayList()): List<Day>
    suspend fun getDayData(day: Int, month: Int, year: Int): List<Holiday>
    suspend fun getHolidaysByTime(time: Time): List<Holiday>
    suspend fun insert(holiday: Holiday): Holiday
    suspend fun insertHolidays(holidays: List<Holiday>)
    suspend fun update(holiday: Holiday)
    suspend fun getFullHolidayData(id: Long, year: Int): Holiday
    suspend fun deleteById(id: Long)
    suspend fun deleteCommonHolidays()
}