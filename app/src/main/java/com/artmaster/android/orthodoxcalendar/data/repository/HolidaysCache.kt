package com.artmaster.android.orthodoxcalendar.data.repository

import com.artmaster.android.orthodoxcalendar.domain.Holiday

object HolidaysCache {
    @Volatile
    var holidays: List<Holiday> = emptyList()
        get() = field.map { it.copy() }
        set(holidays) {
            field = holidays.map { it.copy() }
        }
}
