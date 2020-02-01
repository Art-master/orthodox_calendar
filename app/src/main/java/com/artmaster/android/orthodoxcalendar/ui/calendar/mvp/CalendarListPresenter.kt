package com.artmaster.android.orthodoxcalendar.ui.calendar.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.CalendarListContractPresenter
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.CalendarListContractView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class CalendarListPresenter : MvpPresenter<CalendarListContractView>(), CalendarListContractPresenter {

    private val time = Time()
    private val dispose = CompositeDisposable()

    override fun viewIsReady() {
        viewState.hideActionBar()
        setListPositionByHolidayList()
    }

    private fun setListPositionByHolidayList() {
        val single = Single.fromCallable { DataProvider().getData(time.year) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { data -> setListPosition(data) },
                        onError = { it.printStackTrace() })
        dispose.add(single)

    }

    private fun setListPosition(data: List<HolidayEntity>) {
        viewState.showHolidayList()
        viewState.showActionBar()
        val time = Time()
        for ((index, holiday) in data.withIndex()) {
            if (holiday.month == time.month && holiday.day == time.dayOfMonth) {
                viewState.setInitPosition(index)
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose.clear()
    }
}