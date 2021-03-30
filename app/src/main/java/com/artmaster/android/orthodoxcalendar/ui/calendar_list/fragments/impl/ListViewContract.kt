package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl

import com.artmaster.android.orthodoxcalendar.domain.Holiday
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ListViewContract : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun prepareAdapter(position: Int, holiday: Holiday)
}