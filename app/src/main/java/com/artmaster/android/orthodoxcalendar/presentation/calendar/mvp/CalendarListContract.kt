package com.artmaster.android.orthodoxcalendar.presentation.calendar.mvp

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
        fun showErrorMassage(msgType: Message.ERROR)
    }

    interface Presenter : AppPresenter<View>

    interface Model : AppModel {
        fun getDataSequence(start: Int, size: Int): List<HolidayEntity>
    }
}