package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl

import com.artmaster.android.orthodoxcalendar.domain.Holiday
import moxy.MvpView

interface ListViewContract : MvpView {
    fun prepareAdapter(position: Int, holiday: Holiday)
}