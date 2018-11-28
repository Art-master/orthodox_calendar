package com.artmaster.android.orthodoxcalendar.impl

import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity

interface AppFileParser {
    fun getData(): List<HolidayEntity>
}