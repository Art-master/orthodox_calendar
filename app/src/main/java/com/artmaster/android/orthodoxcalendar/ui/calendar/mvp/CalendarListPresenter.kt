package com.artmaster.android.orthodoxcalendar.ui.calendar.mvp

import android.content.Context
import com.artmaster.android.orthodoxcalendar.impl.mvp.AbstractAppPresenter


class CalendarListPresenter(val context: Context) :
        AbstractAppPresenter<CalendarListContract.View>(), CalendarListContract.Presenter {

    override fun viewIsReady() {
        getView().hideActionBar()
        getView().showHolidayList()
        getView().showActionBar()
    }
}