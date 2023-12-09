package com.artmaster.android.orthodoxcalendar.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday

@Immutable
interface ICalendarViewModel {
    suspend fun loadAllHolidaysOfMonth(monthNumWith0: Int, year: Int)
    fun loadAllHolidaysOfCurrentYear()
    fun getAvailableYears(): List<Int>
    suspend fun loadAllHolidaysOfYear(year: Int)
    fun addActiveFilter(filter: Filter)
    fun removeActiveFilter(filter: Filter)
    fun clearAllFilters()
    fun getActiveFilters(): MutableState<Set<Filter>>
    fun insertHoliday(holiday: Holiday, onComplete: (holiday: Holiday) -> Unit = {})
    fun updateHoliday(holiday: Holiday, onComplete: (holiday: Holiday) -> Unit = {})
    fun deleteHoliday(id: Long, onComplete: (id: Long) -> Unit = {})
    fun setYear(year: Int)
    fun getYear(): MutableState<Int>
    fun setMonth(month: Int)
    fun getMonth(): MutableState<Int>
    fun setDayOfMonth(day: Int)
    fun getDayOfMonth(): MutableState<Int>
    fun getDayOfYear(): MutableState<Int>
    fun setDayOfYear(day: Int)
    fun getCurrentMonthData(monthNum: Int): MutableState<List<Day>>
    fun getCurrentYearData(yearNum: Int): MutableState<List<Day>>
    suspend fun getFullHolidayInfo(holidayId: Long, year: Int): Holiday
    suspend fun getFullHolidayById(id: Long): Holiday
    fun getAllHolidaysOfYear(): List<Holiday>
    fun resetTime()
    fun firstLoadingTileCalendar(): Boolean
}