package com.artmaster.android.orthodoxcalendar.ui.review.impl

import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppPresenter
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppView

interface HolidayReviewContract {
    interface View : AppView {
        fun showErrorMassage(msgType: Message.ERROR)
        fun showHolidayName(name: String)
        fun showDescription(description: String)
        fun showNewStyleDate(date: String)
        fun showOldStyleDate(date: String)
        fun showImageHoliday()
    }

    interface Presenter : AppPresenter<View> {
        fun init(holiday: HolidayEntity)
    }
}