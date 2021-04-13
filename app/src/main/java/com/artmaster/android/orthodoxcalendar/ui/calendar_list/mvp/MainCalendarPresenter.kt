package com.artmaster.android.orthodoxcalendar.ui.calendar_list.mvp

import com.artmaster.android.orthodoxcalendar.ui.calendar_list.impl.CalendarListContractPresenter
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.impl.CalendarListContractView
import moxy.InjectViewState
import moxy.MvpPresenter


@InjectViewState
class MainCalendarPresenter : MvpPresenter<CalendarListContractView>(), CalendarListContractPresenter {

    override fun viewIsReady() {
        viewState.hideActionBar()
        viewState.showHolidayList()
        viewState.showActionBar()
    }
}