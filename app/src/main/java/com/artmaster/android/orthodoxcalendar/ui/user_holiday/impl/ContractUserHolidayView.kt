package com.artmaster.android.orthodoxcalendar.ui.user_holiday.impl

import com.artmaster.android.orthodoxcalendar.domain.Holiday
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ContractUserHolidayView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(holiday: Holiday)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun closeView()
}