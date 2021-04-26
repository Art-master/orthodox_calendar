package com.artmaster.android.orthodoxcalendar.ui.user_holiday.impl

import com.artmaster.android.orthodoxcalendar.domain.Holiday

interface ContractUserHolidayPresenter {
    fun viewIsReady()
    fun viewIsCreated()
    fun dataCanBeSave(holiday: Holiday, needUpdate: Boolean)
}