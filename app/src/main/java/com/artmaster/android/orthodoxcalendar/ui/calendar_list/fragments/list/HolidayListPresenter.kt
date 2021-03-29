package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list

import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListPresenterContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewContract
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class HolidayListPresenter : MvpPresenter<ListViewContract>(), ListPresenterContract {

    private val dispose = CompositeDisposable()
    private var time = Time()

    override fun viewIsReady(time: Time) {
        this.time = time
        getHolidays(time.year)
    }

    private fun getHolidays(year: Int) {
        val disposable = Single.fromCallable {
            val holidays = DataProvider().getData(year)
            calculatePosition(holidays)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { viewData(it) },
                        onError = { it.printStackTrace() })
        dispose.add(disposable)
    }

    private fun calculatePosition(holidays: List<Holiday>): Pair<Int, Holiday> {
        for ((index, holiday) in holidays.withIndex()) {
            if (holiday.monthWith0 >= time.monthWith0 && holiday.day >= time.dayOfMonth) {
                return index to holiday
            }
        }
        return 0 to Holiday()
    }

    private fun viewData(pos: Pair<Int, Holiday>) {
        viewState.prepareAdapter(pos.first, pos.second)
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose.clear()
    }
}