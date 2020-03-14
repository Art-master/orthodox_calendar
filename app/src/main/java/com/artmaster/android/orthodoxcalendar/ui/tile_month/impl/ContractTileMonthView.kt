package com.artmaster.android.orthodoxcalendar.ui.tile_month.impl

import com.arellomobile.mvp.MvpView
import com.artmaster.android.orthodoxcalendar.domain.Day

interface ContractTileMonthView : MvpView {
    fun prepareDayOfMonth(dayOfWeek: Int, level: Int, day: Day)
    fun prepareDaysOfWeekRows(dayOfWeek: IntRange)
    fun setFocus(monthNum: Int)
    fun drawView()
    fun clearView()
}