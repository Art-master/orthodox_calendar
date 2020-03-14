package com.artmaster.android.orthodoxcalendar.ui.calendar_list.impl

import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity

interface CalendarListContractModel {
    fun getDataSequence(start: Int, size: Int, year: Int): List<HolidayEntity>
}