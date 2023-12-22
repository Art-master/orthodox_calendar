package com.artmaster.android.orthodoxcalendar.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import kotlin.random.Random

class CalendarViewModelFake : ViewModel(), ICalendarViewModel {

    private val initTime = Time()

    private var activeFilters: MutableState<Set<Filter>> = mutableStateOf(
        hashSetOf(Filter.BIRTHDAYS, Filter.AVERAGE_HOLIDAYS, Filter.EASTER, Filter.HEAD_HOLIDAYS)
    )

    override suspend fun loadAllHolidaysOfMonth(monthNumWith0: Int, year: Int) {

    }

    override fun loadAllHolidaysOfCurrentYear() {

    }

    override fun getAvailableYears(): List<Int> {
        val size = Constants.HolidayList.PAGE_SIZE.value
        val firstYear = initTime.year - size / 2
        val years = ArrayList<Int>(size)
        for (element in firstYear until firstYear + size) {
            years.add(element)
        }
        return years
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

    override fun deleteHoliday(id: Long, onComplete: ((id: Long) -> Unit)?) {

    }

    override fun setYear(year: Int) {

    }

    override fun getYear() = mutableIntStateOf(initTime.year)

    override fun setMonth(month: Int) {

    }

    override fun getMonth() = mutableIntStateOf(2)

    override fun setDayOfMonth(day: Int) {

    }

    override fun getDayOfMonth() = mutableIntStateOf(2)

    override fun getDayOfYear() = mutableIntStateOf(32)
    override fun setDayOfYear(day: Int) {

    }


    override fun getCurrentMonthData(monthNum: Int): MutableState<List<Day>> {
        return mutableStateOf((1..30).map {
            val dayOfMonth = (it % 30)
            val dayInWeek = (it % 7)
            val holidays = generateHolidays(monthNum, dayOfMonth, dayInWeek)
            Day(initTime.year, monthNum, dayOfMonth, dayInWeek, holidays)
        }
            .toList())
    }

    override fun getCurrentYearData(yearNum: Int): MutableState<List<Day>> {
        return mutableStateOf((1..365).map {
            val month = (it % 11)
            val dayOfMonth = (it % 30)
            val dayInWeek = (it % 7)
            val holidays = generateHolidays(month, dayOfMonth, dayInWeek)
            Day(initTime.year, month, dayOfMonth, dayInWeek, holidays)
        }
            .toList())
    }

    override suspend fun getFullHolidayInfo(holidayId: Long, year: Int): Holiday {
        return Holiday()
    }

    override fun getHolidayById(id: Long): Holiday {
        return Holiday()
    }

    override suspend fun getFullHolidayById(id: Long): Holiday {
        return Holiday()
    }

    override fun getAllHolidaysOfYear(): List<Holiday> {
        return (1..100).map { Holiday() }.toList()
    }

    override fun getNearestHoliday(): Holiday? {
        return Holiday()
    }

    private fun generateHolidays(
        month: Int,
        dayOfMonth: Int,
        dayInWeek: Int,
    ): ArrayList<Holiday> {
        val randomHolidayType = Random.nextInt(Holiday.Type.entries.size - 1)
        return (1..5).map {
            Holiday(
                year = initTime.year,
                month = month,
                day = dayOfMonth,
                title = "Какой-то великий праздник $it",
                id = it.toLong(),
                typeId = Holiday.Type.entries[randomHolidayType].id
            )
        }
            .toList() as ArrayList<Holiday>
    }

    override fun resetTime() {

    }

    override fun firstLoadingTileCalendar() = false
}