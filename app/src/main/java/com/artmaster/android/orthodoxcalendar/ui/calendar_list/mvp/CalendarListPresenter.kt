package com.artmaster.android.orthodoxcalendar.ui.calendar_list.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.impl.CalendarListContractPresenter
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.impl.CalendarListContractView


@InjectViewState
class CalendarListPresenter : MvpPresenter<CalendarListContractView>(), CalendarListContractPresenter {

    override fun viewIsReady() {
        viewState.hideActionBar()
        viewState.showHolidayList()
        viewState.showActionBar()
    }
}