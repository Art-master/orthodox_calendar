package com.artmaster.android.orthodoxcalendar.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_COUNT
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel : ViewModel() {
    private val preferences = App.appComponent.getPreferences()
    private val repository = App.appComponent.getRepository()

    private val currentTime = Time()

    private val dayOfMonth = mutableStateOf(currentTime.dayOfMonth)
    private val month = mutableStateOf(currentTime.month)
    private val year = mutableStateOf(currentTime.year)
    val availableYears = getAvailableYears(currentYear = currentTime.year)

    private val currentHoliday = mutableStateOf<Holiday?>(null)

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

    private val _filters = MutableLiveData<Set<Filter>>(emptySet())
    val filters: LiveData<Set<Filter>> get() = _filters

    private val _time = MutableLiveData(SharedTime())
    val time: LiveData<SharedTime> get() = _time

    init {
        viewModelScope.launch {
            initFilters()
        }
    }

    private suspend fun initFilters() {
        withContext(Dispatchers.IO) {
            val filters = HashSet<Filter>()
            Filter.values().forEach {
                val data = preferences.get(it.settingName)
                if (data == Settings.TRUE) filters.add(it)
            }
            withContext(Dispatchers.Main) {
                if (filters.isNotEmpty()) _filters.value = filters
            }
        }
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

        val prev = daysByMonthCache[monthNumWith0]
        val oldYear = getYear().value
        if (year != oldYear && prev != null && prev.value.isNotEmpty()) {
            return
        }

        withContext(Dispatchers.IO) {
            val value = repository.getMonthDays(monthNumWith0, year, filters.value!!)
            withContext(Dispatchers.Main) {
                val monthData = getCurrentMonthData(monthNumWith0)
                monthData.value = value
            }
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
            val value = repository.getYearDays(year, filters.value!!)
            withContext(Dispatchers.Main) {
                val monthData = getCurrentYearData(year)
                monthData.value = value
            }
        }
    }

    fun addFilter(item: Filter) {
        val copyData = HashSet(_filters.value ?: HashSet())
        copyData.add(item)
        _filters.value = copyData
    }

    fun update() {
        val copyData = HashSet(_filters.value ?: HashSet())
        _filters.value = copyData

        val previous = _time.value ?: SharedTime()
        val obj = SharedTime(previous.year, previous.month, previous.day)
        _time.value = obj
    }

    fun removeFilter(item: Filter) {
        if (_filters.value == null) return
        val copyData = HashSet(_filters.value)
        copyData.remove(item)
        _filters.value = copyData
    }

    fun setYear(year: Int) {
        this.year.value = year
        daysByMonthCache.forEach { it.value.value = emptyList() }
    }

    fun getYear() = year

    fun setMonth(month: Int) {
        this.month.value = month
    }

    fun getMonth() = month

    fun setDayOfMonth(day: Int) {
        dayOfMonth.value = day
    }

    fun getDayOfMonth() = dayOfMonth


    fun getCurrentMonthData(monthNum: Int): MutableState<List<Day>> {
        return daysByMonthCache[monthNum]!!
    }

    fun getCurrentYearData(yearNum: Int): MutableState<List<Day>> {
        return daysByYearsCache[yearNum]!!
    }

    fun loadHolidayAdditionalInfo(holiday: Holiday) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val value = repository.getFullHolidayData(holiday.id, holiday.year)
                withContext(Dispatchers.Main) {
                    val data = getCurrentHoliday()
                    data.value = value
                }
            }
        }
    }

    fun getCurrentHoliday(): MutableState<Holiday?> {
        return currentHoliday
    }

    fun getAllHolidaysOfYear(): List<Holiday> {
        val days = daysByYearsCache[currentTime.year]?.value?.flatMap { d -> d.holidays }
        return days!!
    }
}