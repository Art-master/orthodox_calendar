package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthPresenter
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

@InjectViewState
class TileMonthPresenter : MvpPresenter<ContractTileMonthView>(), ContractTileMonthPresenter {
    private val dispose = CompositeDisposable()
    private var appComponent = App.appComponent
    private val time = Time()

    override fun viewIsReady(year: Int, month: Int) {
        getHolidays(year, month)
        time.calendar.set(year, month, 1)
    }

    private fun getHolidays(year: Int, month: Int){
        val disposable = Single.fromCallable {appComponent.getRepository().getMonthDays(month, year)}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { data -> viewData(data, time) },
                        onError = { it.printStackTrace() })
        dispose.add(disposable)
    }

    override fun viewIsCreated() { }

    private fun viewData(days: List<Day>, time: Time) {
        viewState.createDaysOfWeekRows(1..7)
        val numDays = time.daysInMonth
        for(index in 1..numDays){
            time.calendar.set(Calendar.DAY_OF_MONTH, index)
            val dayOfWeek = time.dayOfWeek
            val week = time.calendar.get(Calendar.WEEK_OF_MONTH)
            viewState.createDay(dayOfWeek, week, days[index])
        }
        viewState.setFocus(time.month -1)
    }

    override fun viewIsPaused() {}

    override fun viewIsResumed() {}

    override fun onDestroy() {
        super.onDestroy()
        dispose.clear()
    }
}