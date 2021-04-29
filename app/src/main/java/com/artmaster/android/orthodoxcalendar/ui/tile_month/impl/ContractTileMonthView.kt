package com.artmaster.android.orthodoxcalendar.ui.tile_month.impl

import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Time
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ContractTileMonthView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun prepareMonthsDays(days: List<Day>, time: Time)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun prepareDaysOfWeekRows(dayOfWeek: IntRange)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFocus()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun drawView()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun clearView()
}