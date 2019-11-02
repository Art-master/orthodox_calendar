package com.artmaster.android.orthodoxcalendar.ui.calendar.mvp

import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppModel
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppPresenter
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppView

interface CalendarListContract {
    interface View : AppView {
        fun showActionBar()
        fun hideActionBar()
        fun showHolidayList()
        fun showErrorMessage(msgType: Message.ERROR)
    }

    interface Presenter : AppPresenter<View>

    interface Model : AppModel {
        fun getDataSequence(start: Int, size: Int, year: Int): List<HolidayEntity>
    }
}