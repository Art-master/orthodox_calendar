package com.artmaster.android.orthodoxcalendar.ui.calendar_list.impl

import com.artmaster.android.orthodoxcalendar.common.msg.Error
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface CalendarListContractView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showActionBar()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideActionBar()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showHolidayList()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showErrorMessage(msgType: Error)
}