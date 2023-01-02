package com.artmaster.android.orthodoxcalendar.data.repository

import com.artmaster.android.orthodoxcalendar.domain.Holiday

object HolidaysCache {

    private var holidays: List<Holiday> = emptyList()

    fun clear() {
        holidays = emptyList()
    }

    fun getCopy() = holidays.map { it.copy() }

    fun isNotEmpty() = holidays.isNotEmpty()

    fun set(data: List<Holiday>) {
        holidays = data
    }
}
