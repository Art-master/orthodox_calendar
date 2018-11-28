package com.artmaster.android.orthodoxcalendar.presentation.init.mvp

import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppModel
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppPresenter
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppView
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity

interface InitAppContract {
    interface View : AppView {
        fun showLoadingScreen()
        fun showErrorMassage(msgType: Message.ERROR)
        fun nextScreen()
    }

    interface Presenter : AppPresenter<View> {
        fun loadingScreenIsShowed()
    }

    interface Model : AppModel {
        fun getDataFromFile(): List<HolidayEntity>
        fun fillDatabase(data: List<HolidayEntity>)
    }
}