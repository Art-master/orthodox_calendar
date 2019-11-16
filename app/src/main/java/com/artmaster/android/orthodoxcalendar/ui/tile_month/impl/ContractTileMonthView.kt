package com.artmaster.android.orthodoxcalendar.ui.tile_month.impl

import com.arellomobile.mvp.MvpView
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity

interface ContractTileMonthView : MvpView {
    fun createDay(dayOfWeek: Int, level: Int, holidays: List<HolidayEntity>, i: Int)
    fun createDayOfWeekName(dayOfWeek: Int)
    fun createDaysOfWeekRows(dayOfWeek : IntRange)
    fun clearView()
}