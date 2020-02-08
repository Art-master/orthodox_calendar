package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl

import com.arellomobile.mvp.MvpView
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity

interface ListViewContract : MvpView {
    fun prepareAdapter(position: Int, holiday: HolidayEntity)
}