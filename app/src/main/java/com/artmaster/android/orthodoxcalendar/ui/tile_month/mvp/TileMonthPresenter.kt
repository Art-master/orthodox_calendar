package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time2
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthPresenter
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

@InjectViewState
class TileMonthPresenter : MvpPresenter<ContractTileMonthView>(), ContractTileMonthPresenter {
    private val dispose = CompositeDisposable()
    private var appComponent = App.appComponent
    private var holidayList = emptyList<HolidayEntity>()
    private val time = Time2()

    override fun viewIsReady() {
        getHolidays(time)
    }

    private fun getHolidays(time: Time2){
        val disposable = Single.fromCallable {appComponent.getRepository().getMonthData(time.month, time.year)}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { data -> holidayList = data },
                        onError = { it.printStackTrace() })
        dispose.add(disposable)
    }

    override fun viewIsCreated() {
        viewData(holidayList, time)
    }

    private fun viewData(data: List<HolidayEntity>, time: Time2) {
        viewState.createDaysOfWeekRows(1..7)
        val numDays = time.daysInMonth
        for(i in 1..numDays){
            time.calendar.set(Calendar.DAY_OF_MONTH, i)
            val dayOfWeek = time.dayOfWeek
            val e = time.calendar.get(Calendar.WEEK_OF_MONTH)
            viewState.createDay(dayOfWeek, e, sortByDay(i, data), i)
        }
    }

    private fun sortByDay(day: Int, data: List<HolidayEntity>): ArrayList<HolidayEntity> {
        val holidaysInDay = ArrayList<HolidayEntity>()
        for(holiday in data){
            if(holiday.day == day) holidaysInDay.add(holiday)
        }
        return holidaysInDay
    }

    override fun viewIsPaused() {}

    override fun viewIsResumed() {}

    override fun onDestroy() {
        super.onDestroy()
        dispose.clear()
    }
}