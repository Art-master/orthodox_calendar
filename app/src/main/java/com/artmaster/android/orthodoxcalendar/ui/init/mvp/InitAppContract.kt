package com.artmaster.android.orthodoxcalendar.ui.init.mvp

import com.artmaster.android.orthodoxcalendar.common.msg.Error
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppModel
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppPresenter
import com.artmaster.android.orthodoxcalendar.impl.mvp.AppView

interface InitAppContract {
    interface View : AppView {
        fun showLoadingScreen(timeAnimation: Long)
        fun showErrorMessage(msgType: Error)
        fun initNotifications()
        fun nextScreen()
    }

    interface Presenter : AppPresenter<View>

    interface Model : AppModel {
        fun getDataFromFile(): List<Holiday>
        fun fillDatabase(data: List<Holiday>)
    }
}