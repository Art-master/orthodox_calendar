package com.artmaster.android.orthodoxcalendar.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
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
import java.util.Calendar

@Immutable
class CalendarViewModel : ViewModel(), ICalendarViewModel {
    private val preferences = App.appComponent.getPreferences()
    private val repository = App.appComponent.getRepository()

    private val initTime = Time()

    private val dayOfMonth = mutableIntStateOf(initTime.dayOfMonth)
    private val dayOfYear = mutableIntStateOf(initTime.dayOfYear)
    private val month = mutableIntStateOf(initTime.monthWith0)
    private val year = mutableIntStateOf(initTime.year)
    private val availableYears = getAvailableYears(currentYear = initTime.year)

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

    private var activeFilters: MutableState<Set<Filter>> = mutableStateOf(hashSetOf())

    init {
        initFilters()
    }

    private fun initFilters() {
        val filters = HashSet<Filter>()
        Filter.entries.forEach {
            val data = preferences.get(it.settingName)
            if (data == Settings.TRUE) {
                it.enabled = true
                filters.add(it)
            }
        }

        this@CalendarViewModel.activeFilters = mutableStateOf(filters)
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

    override suspend fun loadAllHolidaysOfMonth(monthNumWith0: Int, year: Int) {
        if (monthNumWith0 !in 0..MONTH_COUNT.dec()) return

        withContext(Dispatchers.IO) {
            val value = repository.getMonthDays(monthNumWith0, year, activeFilters.value)
            withContext(Dispatchers.Main) {
                val monthData = getCurrentMonthData(monthNumWith0)
                monthData.value = value
            }
        }
    }

    override fun loadAllHolidaysOfCurrentYear() {
        viewModelScope.launch {
            loadAllHolidaysOfYear(year.value)
        }
    }

    override fun getAvailableYears(): List<Int> {
        return availableYears
    }

    override suspend fun loadAllHolidaysOfYear(year: Int) {
        if (year !in availableYears.first()..availableYears.last()) return

        val prev = daysByYearsCache[year]
        val oldYear = getYear().value
        if (year != oldYear && prev != null && prev.value.isNotEmpty()) {
            return
        }

        withContext(Dispatchers.IO) {
            val value = repository.getYearDays(year, activeFilters.value)
            withContext(Dispatchers.Main) {
                val data = getCurrentYearData(year)
                data.value = value
            }
        }
    }

    override fun addActiveFilter(filter: Filter) {
        val copyData = HashSet(activeFilters.value)
        copyData.add(filter)
        activeFilters.value = copyData
        filter.enabled = true
        saveFilter(filter)
    }

    private fun saveFilter(filter: Filter) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferences.set(filter.settingName, filter.enabled.toString())
            }
        }
    }

    override fun removeActiveFilter(filter: Filter) {
        val copyData = HashSet(activeFilters.value)
        copyData.remove(filter)
        activeFilters.value = copyData
        filter.enabled = false
        saveFilter(filter)
    }

    override fun clearAllFilters() {
        activeFilters.value = HashSet()
        Filter.entries.forEach { it.enabled = false }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Filter.entries.forEach {
                    preferences.set(it.settingName, it.enabled.toString())
                }
            }
        }
    }


    override fun getActiveFilters(): MutableState<Set<Filter>> {
        return activeFilters
    }

    override fun insertHoliday(holiday: Holiday, onComplete: (holiday: Holiday) -> Unit) {
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

    override fun updateHoliday(holiday: Holiday, onComplete: (holiday: Holiday) -> Unit) {
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

    override fun deleteHoliday(id: Long, onComplete: ((id: Long) -> Unit)?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteById(id)
                clearCaches()
                withContext(Dispatchers.Main) {
                    onComplete?.invoke(id)
                }
            }
        }
    }

    override fun setYear(year: Int) {
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

    override fun getYear() = year

    override fun setMonth(month: Int) {
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
        if (dayOfMonth.intValue > initTime.daysInMonth) {
            dayOfMonth.intValue = initTime.daysInMonth
        }
    }

    override fun getMonth() = month

    override fun setDayOfMonth(day: Int) {
        dayOfMonth.intValue = day

        initTime.calendar.apply {
            set(Calendar.YEAR, year.intValue)
            set(Calendar.MONTH, month.intValue)
            set(Calendar.DAY_OF_MONTH, dayOfMonth.intValue)
        }

        dayOfYear.intValue = initTime.dayOfYear
    }

    override fun getDayOfMonth() = dayOfMonth

    override fun getDayOfYear() = dayOfYear
    override fun setDayOfYear(day: Int) {
        dayOfYear.intValue = day

        initTime.calendar.set(year.intValue, month.intValue, day)
        dayOfMonth.intValue = initTime.dayOfMonth
    }


    override fun getCurrentMonthData(monthNum: Int): MutableState<List<Day>> {
        return daysByMonthCache[monthNum]!!
    }

    override fun getCurrentYearData(yearNum: Int): MutableState<List<Day>> {
        return daysByYearsCache[yearNum]!!
    }

    override suspend fun getFullHolidayInfo(holidayId: Long, year: Int): Holiday {
        return withContext(Dispatchers.IO) {
            repository.getFullHolidayData(holidayId, year)
        }
    }

    override suspend fun getFullHolidayById(id: Long): Holiday {
        return withContext(Dispatchers.IO) {
            repository.getFullHolidayData(id, year.intValue)
        }
    }

    override fun getHolidayById(id: Long): Holiday {
        for (data in daysByYearsCache.entries) {
            for (day in data.value.value) {
                for (holiday in day.holidays) {
                    if (holiday.id == id) {
                        return holiday
                    }
                }
            }
        }
        throw IllegalStateException("Не найдено праздника id=$id")
    }

    override fun getAllHolidaysOfYear(): List<Holiday> {
        val days = daysByYearsCache[year.intValue]?.value?.flatMap { d -> d.holidays }
        return days!!
    }

    override fun getNearestHoliday(): Holiday? {
        val data = getCurrentYearData(year.intValue).value
        if (data.isEmpty()) return null

        val holidays = data[dayOfYear.intValue.dec()].holidays
        return if (holidays.isNotEmpty()) {
            holidays.first()
        } else {
            getNextHoliday(dayOfYear.intValue)
        }
    }

    private fun getNextHoliday(currentDayNum: Int): Holiday? {
        val data = getCurrentYearData(year.intValue).value
        for (i in currentDayNum until data.size) {
            val holidays = data[i.dec()].holidays
            if (holidays.isNotEmpty()) return holidays.first()
        }
        for (i in currentDayNum downTo 1) {
            val holidays = data[i.dec()].holidays
            if (holidays.isNotEmpty()) return holidays.first()
        }
        return null
    }

    override fun resetTime() {
        val now = Time()
        if (now.year != year.intValue) {
            setYear(now.year)
        }
        if (now.monthWith0 != month.intValue) {
            setMonth(now.monthWith0)
        }
        if (now.dayOfMonth != dayOfMonth.intValue) {
            setDayOfMonth(now.dayOfMonth)
        }
    }

    override fun firstLoadingTileCalendar() =
        preferences.get(Settings.Name.FIRST_LOADING_TILE_CALENDAR).toBoolean()
}