package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
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
    private val time = Time()

    private var flag = false

    override fun viewIsReady(year: Int, month: Int) {
        time.calendar.set(year, month, 1)
        getHolidays(year, month)
    }

    private fun getHolidays(year: Int, month: Int){
        val disposable = Single.fromCallable {
            val days = DataProvider().getMonthDays(month, year)
            prepareView(days, time)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { viewData(time) },
                        onError = { it.printStackTrace() })
        dispose.add(disposable)
    }

    override fun viewIsCreated() {
        flag = true
    }

    private fun prepareView(days: List<Day>, time: Time) {
        while (!flag) {
        }

        viewState.prepareDaysOfWeekRows(1..7)
        val numDays = time.daysInMonth
        for(index in 1..numDays){
            time.calendar.set(Calendar.DAY_OF_MONTH, index)
            val dayOfWeek = time.dayOfWeek
            val week = time.calendar.get(Calendar.WEEK_OF_MONTH)
            viewState.prepareDayOfMonth(dayOfWeek, week, days[index - 1])
        }
    }

    private fun viewData(time: Time) {
        viewState.clearView()
        viewState.drawView()
        viewState.setFocus(time.month -1)
    }

    override fun viewIsPaused() {
    }

    override fun viewIsResumed() {}

    override fun onDestroy() {
        super.onDestroy()
        dispose.clear()
    }
}