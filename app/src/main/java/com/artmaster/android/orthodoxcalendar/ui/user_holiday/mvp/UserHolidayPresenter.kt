package com.artmaster.android.orthodoxcalendar.ui.user_holiday.mvp

import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.impl.ContractUserHolidayPresenter
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.impl.ContractUserHolidayView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class UserHolidayPresenter : MvpPresenter<ContractUserHolidayView>(), ContractUserHolidayPresenter {

    val database = App.appComponent.getDatabase()
    val context = App.appComponent.getContext()

    override fun viewIsReady() {

    }

    override fun viewIsCreated() {

    }

    override fun dataCanBeSave(holiday: Holiday) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                database.get(context).holidayDao().insertHoliday(holiday)
                withContext(Dispatchers.Main) {
                    viewState.closeView()
                }
            }
        }
    }

}