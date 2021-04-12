package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.domain.Filter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarSharedData : ViewModel() {
    private val preferences = App.appComponent.getPreferences()

    private val _filters = MutableLiveData<Set<Filter>>()
    val filters: LiveData<Set<Filter>> get() = _filters

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
}