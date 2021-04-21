package com.artmaster.android.orthodoxcalendar.ui.review.impl

import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppPresenter
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppView

interface HolidayReviewContract {
    interface View : AppView {
        fun showErrorMessage(msgType: Message.ERROR)
        fun showHolidayName(name: String)
        fun showDescription(initialLater: String, description: String)
        fun showNewStyleDate(date: String)
        fun showOldStyleDate(date: String)
        fun showImageHoliday(resId: Int, placeholderId: Int)
    }

    interface Presenter : AppPresenter<View> {
        fun init(id: Long)
    }
}