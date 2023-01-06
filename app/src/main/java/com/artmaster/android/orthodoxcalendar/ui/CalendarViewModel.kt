package com.artmaster.android.orthodoxcalendar.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_COUNT
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CalendarViewModel : ViewModel() {
    private val preferences = App.appComponent.getPreferences()
    private val repository = App.appComponent.getRepository()

    private val initTime = Time()

    private val dayOfMonth = mutableStateOf(initTime.dayOfMonth)
    private val dayOfYear = mutableStateOf(initTime.dayOfYear)
    private val month = mutableStateOf(initTime.monthWith0)
    private val year = mutableStateOf(initTime.year)
    val availableYears = getAvailableYears(currentYear = initTime.year)

    private val daysByMonthCache = HashMap<Int, MutableState<List<Day>>>(MONTH_COUNT)
        .apply {
            for (num in 0 until MONTH_COUNT) this[num] = mutableStateOf(ArrayList())
        }

    private val daysByYearsCache = HashMap<Int, MutableState<List<Day>>>(availableYears.size)
        .apply {
            for (num in availableYears.indices) {
                this[availableYears.first() + num] = mutableStateOf(ArrayList())
            }
        }

    private lateinit var filters: MutableState<Set<Filter>>

    init {
        initFilters()
    }

    private fun initFilters() {
        val filters = HashSet<Filter>()
        Filter.values().forEach {
            val data = preferences.get(it.settingName)
            if (data == Settings.TRUE) filters.add(it)
        }

        this@CalendarViewModel.filters = mutableStateOf(filters)
    }

    private fun getAvailableYears(currentYear: Int): List<Int> {
        val size = Constants.HolidayList.PAGE_SIZE.value
        val firstYear = currentYear - size / 2
        val years = ArrayList<Int>(size)
        for (element in firstYear until firstYear + size) {
            years.add(element)
        }
        return years
    }

    suspend fun loadAllHolidaysOfMonth(monthNumWith0: Int, year: Int) {
        if (monthNumWith0 !in 0..MONTH_COUNT.dec()) return

        withContext(Dispatchers.IO) {
            val value = repository.getMonthDays(monthNumWith0, year, filters.value)
            withContext(Dispatchers.Main) {
                val monthData = getCurrentMonthData(monthNumWith0)
                monthData.value = value
            }
        }
    }

    fun loadAllHolidaysOfCurrentYear() {
        viewModelScope.launch {
            loadAllHolidaysOfYear(year.value)
        }
    }

    suspend fun loadAllHolidaysOfYear(year: Int) {
        if (year !in availableYears.first()..availableYears.last()) return

        val prev = daysByYearsCache[year]
        val oldYear = getYear().value
        if (year != oldYear && prev != null && prev.value.isNotEmpty()) {
            return
        }

        withContext(Dispatchers.IO) {
            val value = repository.getYearDays(year, filters.value)
            withContext(Dispatchers.Main) {
                val data = getCurrentYearData(year)
                data.value = value
            }
        }
    }

    fun addFilter(item: Filter) {
        val copyData = HashSet(filters.value)
        copyData.add(item)
        filters.value = copyData
        item.enabled = true
    }

    fun removeFilter(item: Filter) {
        val copyData = HashSet(filters.value)
        copyData.remove(item)
        filters.value = copyData
        item.enabled = false
    }

    fun clearAllFilters() {
        filters.value = HashSet()
        Filter.values().forEach { it.enabled = false }
    }


    fun getFilters(): MutableState<Set<Filter>> {
        return filters
    }

    fun insertHoliday(holiday: Holiday, onComplete: (holiday: Holiday) -> Unit = {}) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insert(holiday)
                clearCaches()
                withContext(Dispatchers.Main) {
                    onComplete(holiday)
                }
            }
        }
    }

    fun updateHoliday(holiday: Holiday, onComplete: (holiday: Holiday) -> Unit = {}) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.update(holiday)
                clearCaches()
                withContext(Dispatchers.Main) {
                    onComplete(holiday)
                }
            }
        }
    }

    fun deleteHoliday(id: Long, onComplete: (id: Long) -> Unit = {}) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteById(id)
                clearCaches()
                withContext(Dispatchers.Main) {
                    onComplete(id)
                }
            }
        }
    }

    fun setYear(year: Int) {
        clearMonthCache()
        this.year.value = year
    }

    private fun clearMonthCache() {
        daysByMonthCache.forEach { it.value.value = emptyList() }
    }

    private fun clearCaches() {
        daysByMonthCache.forEach { it.value.value = emptyList() }
        daysByYearsCache.forEach { it.value.value = emptyList() }
    }

    fun getYear() = year

    fun setMonth(month: Int) {
        checkDayNumberForThisMonth(month)

        this.month.value = month
    }

    private fun checkDayNumberForThisMonth(month: Int) {
        if (dayOfMonth.value < 28) return

        initTime.calendar.apply {
            set(Calendar.YEAR, year.value)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 0)
        }
        if (dayOfMonth.value > initTime.daysInMonth) {
            dayOfMonth.value = initTime.daysInMonth
        }
    }

    fun getMonth() = month

    fun setDayOfMonth(day: Int) {
        dayOfMonth.value = day

        initTime.calendar.apply {
            set(Calendar.YEAR, year.value)
            set(Calendar.MONTH, month.value)
            set(Calendar.DAY_OF_MONTH, dayOfMonth.value)
        }

        dayOfYear.value = initTime.dayOfYear
    }

    fun getDayOfMonth() = dayOfMonth

    fun getDayOfYear() = dayOfYear
    fun setDayOfYear(day: Int) {
        dayOfYear.value = day

        initTime.calendar.set(year.value, month.value, day)
        dayOfMonth.value = initTime.dayOfMonth
    }


    fun getCurrentMonthData(monthNum: Int): MutableState<List<Day>> {
        return daysByMonthCache[monthNum]!!
    }

    fun getCurrentYearData(yearNum: Int): MutableState<List<Day>> {
        return daysByYearsCache[yearNum]!!
    }

    suspend fun getFullHolidayInfo(holidayId: Long, year: Int): Holiday {
        return withContext(Dispatchers.IO) {
            repository.getFullHolidayData(holidayId, year)
        }
    }

    suspend fun getFullHolidayById(id: Long): Holiday {
        return withContext(Dispatchers.IO) {
            repository.getFullHolidayData(id, year.value)
        }
    }

    fun getAllHolidaysOfYear(): List<Holiday> {
        val days = daysByYearsCache[year.value]?.value?.flatMap { d -> d.holidays }
        return days!!
    }

    fun resetTime() {
        val now = Time()
        if (now.year != year.value) {
            setYear(now.year)
        }
        if (now.monthWith0 != month.value) {
            setMonth(now.monthWith0)
        }
        if (now.dayOfMonth != dayOfMonth.value) {
            setDayOfMonth(now.dayOfMonth)
        }
    }

    fun firstLoadingTileCalendar() =
        preferences.get(Settings.Name.FIRST_LOADING_TILE_CALENDAR).toBoolean()
}