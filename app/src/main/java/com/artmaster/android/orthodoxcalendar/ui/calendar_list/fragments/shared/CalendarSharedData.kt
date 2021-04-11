package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artmaster.android.orthodoxcalendar.domain.Filter

class CalendarSharedData : ViewModel() {
    private val _filters = MutableLiveData<Set<Filter>>()
    val filters: LiveData<Set<Filter>> get() = _filters

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