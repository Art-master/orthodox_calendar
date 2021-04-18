package com.artmaster.android.orthodoxcalendar.ui.user_holiday.mvp

import android.content.Context
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.impl.mvp.AbstractAppPresenter
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.impl.ContractUserHolidayPresenter
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.impl.ContractUserHolidayView

class UserHolidayPresenter(private val context: Context) :
        AbstractAppPresenter<ContractUserHolidayView>(), ContractUserHolidayPresenter {
    override fun viewIsReady() {

    }

    override fun viewIsCreated() {

    }

    override fun dataCanBeSave(holiday: Holiday) {

    }

}