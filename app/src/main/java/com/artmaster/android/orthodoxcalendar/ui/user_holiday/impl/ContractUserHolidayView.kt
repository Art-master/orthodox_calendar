package com.artmaster.android.orthodoxcalendar.ui.user_holiday.impl

import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppView
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ContractUserHolidayView : MvpView, AppView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(holiday: Holiday)
}