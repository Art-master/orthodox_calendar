package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.SharedTime
import com.artmaster.android.orthodoxcalendar.domain.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel : ViewModel() {
    private val preferences = App.appComponent.getPreferences()
    private val repository = App.appComponent.getRepository()

    val daysByMonth = HashMap<Int, MutableLiveData<List<Day>>>()

    private val _filters = MutableLiveData<Set<Filter>>(emptySet())
    val filters: LiveData<Set<Filter>> get() = _filters

    private val _time = MutableLiveData(SharedTime())
    val time: LiveData<SharedTime> get() = _time

    init {
        initTime()
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

    public suspend fun loadMonthData(monthNum: Int, year: Int) {
        daysByMonth.getOrPut(monthNum) {
            MutableLiveData(ArrayList())
        }.value = emptyList()

        withContext(Dispatchers.IO) {
            time.value?.let {
                val value = repository.getMonthDays(monthNum, it.year, filters.value!!)
                withContext(Dispatchers.Main) {
                    daysByMonth.getOrPut(monthNum) {
                        MutableLiveData(ArrayList(value))
                    }.value = value
                }
            }
        }
    }

    private fun initTime() {
        val time = Time()
        setAllTime(time.year, time.monthWith0, time.dayOfMonth)
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
        val previous = _time.value ?: SharedTime()
        val obj = SharedTime(year, previous.month, previous.day)
        _time.value = obj

        if (previous.year != year) {
            viewModelScope.launch {
                loadMonthData(monthNum = obj.month, year = obj.year)
            }
        }
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

    fun getSelectedTime(): Time {
        return Time(time.value!!.year, time.value!!.month, time.value!!.day)
    }

    fun getCurrentMonthData(monthNum: Int) = daysByMonth.getOrPut(monthNum) {
        MutableLiveData(ArrayList())
    }
}