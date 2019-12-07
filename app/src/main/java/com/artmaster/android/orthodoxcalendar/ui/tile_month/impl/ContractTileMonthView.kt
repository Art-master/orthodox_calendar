package com.artmaster.android.orthodoxcalendar.ui.tile_month.impl

import com.arellomobile.mvp.MvpView
import com.artmaster.android.orthodoxcalendar.domain.Day

interface ContractTileMonthView : MvpView {
    fun drawDay(dayOfWeek: Int, level: Int, day: Day)
    fun drawDayOfWeekName(dayOfWeek: Int)
    fun drawDaysOfWeekRows(dayOfWeek : IntRange)
    fun setFocus(monthNum: Int)
    fun clearView()
}