package com.artmaster.android.orthodoxcalendar.ui.init.mvp

import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.*
import com.artmaster.android.orthodoxcalendar.impl.mvp.AbstractAppPresenter
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class InitAppPresenter(
        private var appPreferences: AppPreferences,
        private val model: InitAppContract.Model) :
        AbstractAppPresenter<InitAppContract.View>(), InitAppContract.Presenter {

    override fun viewIsReady() {
        Single.fromCallable {
            getData()
            getView().showLoadingScreen()
        }
                .delay(Constants.LOADING_ANIMATION.toLong().shl(1), TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { getView().nextScreen() },
                        onError = {
                            it.printStackTrace()
                            getView().showErrorMassage(Message.ERROR.INIT_DATABASE)
                        })
    }

    private fun getData(): List<HolidayEntity> {
        var data: List<HolidayEntity> = emptyList()
        if (isAppFirstLoad()) {
            data = model.getDataFromFile()
            model.fillDatabase(data)
            appPreferences.set(FIRST_LOAD_APP, "FALSE")
        }
        return data
    }

    private fun isAppFirstLoad(): Boolean {
        val preference = appPreferences.get(FIRST_LOAD_APP)
        return preference.equals(Settings.EMPTY)
    }
}