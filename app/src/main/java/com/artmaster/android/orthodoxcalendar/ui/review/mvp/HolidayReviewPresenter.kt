package com.artmaster.android.orthodoxcalendar.ui.review.mvp

import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.impl.mvp.AbstractAppPresenter
import com.artmaster.android.orthodoxcalendar.ui.review.impl.HolidayReviewContract

class HolidayReviewPresenter : AbstractAppPresenter<HolidayReviewContract.View>(),
        HolidayReviewContract.Presenter {

    private var holidayEntity: HolidayEntity? = null

    override fun init(holiday: HolidayEntity) {
        holidayEntity = holiday
    }

    override fun viewIsReady() {
        if (holidayEntity == null) throw NoSuchElementException()
        getView().showHolidayName(holidayEntity!!.title)
        getView().showDescription(holidayEntity!!.description)
    }
}