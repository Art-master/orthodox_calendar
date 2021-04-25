package com.artmaster.android.orthodoxcalendar.ui.review.impl

import com.artmaster.android.orthodoxcalendar.common.msg.Error
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface HolidayReviewContract {
    interface View : MvpView {
        @StateStrategyType(AddToEndSingleStrategy::class)
        fun showErrorMessage(msgType: Error)

        @StateStrategyType(AddToEndSingleStrategy::class)
        fun showHolidayName(name: String)

        @StateStrategyType(AddToEndSingleStrategy::class)
        fun showDescription(initialLater: String, description: String)

        @StateStrategyType(AddToEndSingleStrategy::class)
        fun showNewStyleDate(date: String, isCustomHoliday: Boolean)

        @StateStrategyType(AddToEndSingleStrategy::class)
        fun showOldStyleDate(date: String)

        @StateStrategyType(AddToEndSingleStrategy::class)
        fun showImageHoliday(resId: Int, placeholderId: Int)

        @StateStrategyType(AddToEndSingleStrategy::class)
        fun initButtons(holiday: Holiday)
    }

    interface Presenter {
        fun init(id: Long)
        fun viewIsReady()
        fun removeHoliday()
    }
}