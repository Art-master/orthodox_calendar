package com.artmaster.android.orthodoxcalendar.impl

import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity

interface AppDataProvider {
    fun getDataSequence(start: Int, size: Int): List<HolidayEntity>
    fun getData(): List<HolidayEntity>
}