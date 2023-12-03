package com.artmaster.android.orthodoxcalendar.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday

class CalendarViewModelFake : ViewModel(), ICalendarViewModel {

    private var activeFilters: MutableState<Set<Filter>> = mutableStateOf(
        hashSetOf(Filter.BIRTHDAYS, Filter.AVERAGE_HOLIDAYS)
    )

    override suspend fun loadAllHolidaysOfMonth(monthNumWith0: Int, year: Int) {

    }

    override fun loadAllHolidaysOfCurrentYear() {

    }

    override suspend fun loadAllHolidaysOfYear(year: Int) {

    }

    override fun addActiveFilter(filter: Filter) {
        val copyData = HashSet(activeFilters.value)
        copyData.add(filter)
        activeFilters.value = copyData
        filter.enabled = true
    }

    override fun removeActiveFilter(filter: Filter) {
        val copyData = HashSet(activeFilters.value)
        copyData.remove(filter)
        activeFilters.value = copyData
        filter.enabled = false
    }

    override fun clearAllFilters() {
        activeFilters.value = HashSet()
        Filter.entries.forEach { it.enabled = false }
    }


    override fun getActiveFilters(): MutableState<Set<Filter>> {
        return activeFilters
    }

    override fun insertHoliday(holiday: Holiday, onComplete: (holiday: Holiday) -> Unit) {

    }

    override fun updateHoliday(holiday: Holiday, onComplete: (holiday: Holiday) -> Unit) {

    }

    override fun deleteHoliday(id: Long, onComplete: (id: Long) -> Unit) {

    }

    override fun setYear(year: Int) {

    }

    override fun getYear() = mutableStateOf(2022)

    override fun setMonth(month: Int) {

    }

    override fun getMonth() = mutableStateOf(2)

    override fun setDayOfMonth(day: Int) {

    }

    override fun getDayOfMonth() = mutableStateOf(2)

    override fun getDayOfYear() = mutableStateOf(32)
    override fun setDayOfYear(day: Int) {

    }


    override fun getCurrentMonthData(monthNum: Int): MutableState<List<Day>> {
        return mutableStateOf(emptyList())
    }

    override fun getCurrentYearData(yearNum: Int): MutableState<List<Day>> {
        return mutableStateOf(emptyList())
    }

    override suspend fun getFullHolidayInfo(holidayId: Long, year: Int): Holiday {
        return Holiday()
    }

    override suspend fun getFullHolidayById(id: Long): Holiday {
        return Holiday()
    }

    override fun getAllHolidaysOfYear(): List<Holiday> {
        return emptyList()
    }

    override fun resetTime() {

    }

    override fun firstLoadingTileCalendar() = false
}