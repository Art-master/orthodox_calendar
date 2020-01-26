package com.artmaster.android.orthodoxcalendar.ui.calendar.mvp

import android.content.Context
import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.impl.mvp.AbstractAppPresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CalendarListPresenter(val context: Context) :
        AbstractAppPresenter<CalendarListContract.View>(), CalendarListContract.Presenter {

    val time = Time()

    override fun viewIsReady() {
        getView().hideActionBar()
        getView().showHolidayList()
        getView().showActionBar()
        setListPositionByHolidayList()
    }

    private fun setListPositionByHolidayList() {
        Single.fromCallable { DataProvider().getData(time.year) }
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { data -> setListPosition(data) },
                        onError = { it.printStackTrace() })

    }

    private fun setListPosition(data: List<HolidayEntity>) {
        val time = Time()
        for ((index, holiday) in data.withIndex()) {
            if (holiday.month == time.month && holiday.day == time.dayOfMonth) {
                getView().setInitPosition(index)
                return
            }
        }
    }
}