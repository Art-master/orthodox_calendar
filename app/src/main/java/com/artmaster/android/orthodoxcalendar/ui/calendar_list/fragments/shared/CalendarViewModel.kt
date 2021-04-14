package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.SharedTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel : ViewModel() {
    private val preferences = App.appComponent.getPreferences()

    private val _filters = MutableLiveData<Set<Filter>>()
    val filters: LiveData<Set<Filter>> get() = _filters

    private val _time = MutableLiveData(SharedTime())
    val time: LiveData<SharedTime> get() = _time

    init {
        GlobalScope.launch {
            initFilters()
        }
    }

    private suspend fun initFilters() {
        withContext(Dispatchers.IO) {
            val filters = HashSet<Filter>()
            Filter.values().forEach {
                val data = preferences.has(it.settingName)
                if (data) filters.add(it)
            }
            withContext(Dispatchers.Main) {
                if (filters.isNotEmpty()) _filters.value = filters
            }
        }
    }

    fun addFilter(item: Filter) {
        val copyData = HashSet(_filters.value ?: HashSet())
        copyData.add(item)
        _filters.value = copyData
    }

    fun removeFilter(item: Filter) {
        if (_filters.value == null) return
        val copyData = HashSet(_filters.value)
        copyData.remove(item)
        _filters.value = copyData
    }

    fun setYear(year: Int) {
        val previous = _time.value ?: SharedTime()
        val obj = SharedTime(year, previous.month, previous.day)
        _time.value = obj
    }

    fun setMonth(month: Int) {
        val previous = _time.value ?: SharedTime()
        val obj = SharedTime(previous.year, month, previous.day)
        _time.value = obj
    }

    fun setDayOfMonth(day: Int) {
        val previous = _time.value ?: SharedTime()
        val obj = SharedTime(previous.year, previous.month, day)
        _time.value = obj
    }

    fun setAllTime(year: Int? = null, month: Int? = null, day: Int? = null) {
        val previous = _time.value ?: SharedTime()
        val obj = SharedTime(year ?: previous.year, month ?: previous.month, day ?: previous.day)
        _time.value = obj
    }
}