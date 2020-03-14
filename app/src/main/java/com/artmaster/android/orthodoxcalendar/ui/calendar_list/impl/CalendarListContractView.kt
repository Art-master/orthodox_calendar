package com.artmaster.android.orthodoxcalendar.ui.calendar_list.impl

import com.arellomobile.mvp.MvpView
import com.artmaster.android.orthodoxcalendar.common.Message

interface CalendarListContractView : MvpView {
    fun showActionBar()
    fun hideActionBar()
    fun showHolidayList()
    fun showErrorMessage(msgType: Message.ERROR)
}