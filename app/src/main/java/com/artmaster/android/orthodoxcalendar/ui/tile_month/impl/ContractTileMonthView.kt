package com.artmaster.android.orthodoxcalendar.ui.tile_month.impl

import com.artmaster.android.orthodoxcalendar.domain.Day
import moxy.MvpView

interface ContractTileMonthView : MvpView {
    fun prepareDayOfMonth(dayOfWeek: Int, level: Int, day: Day)
    fun prepareDaysOfWeekRows(dayOfWeek: IntRange)
    fun setFocus(monthNum: Int)
    fun drawView()
    fun clearView()
}