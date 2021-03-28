package com.artmaster.android.orthodoxcalendar.ui.calendar_list.impl

import com.artmaster.android.orthodoxcalendar.common.Message
import moxy.MvpView

interface CalendarListContractView : MvpView {
    fun showActionBar()
    fun hideActionBar()
    fun showHolidayList()
    fun showErrorMessage(msgType: Message.ERROR)
}