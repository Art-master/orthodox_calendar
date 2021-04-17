package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list

import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListPresenterContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewContract
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class HolidayListPresenter : MvpPresenter<ListViewContract>(), ListPresenterContract {

    private var time = Time()

    private var job: Job? = null

    override suspend fun viewIsReady(time: Time, filters: List<Filter>) {
        this.time = time
        job = prepareDataAndView(time.year, filters)
    }

    private suspend fun prepareDataAndView(year: Int, filters: List<Filter>): Job {
        return GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val holidays = DataProvider().getData(year, filters)
                val position = calculatePosition(holidays)
                viewState.prepareAdapter()
                delay(1000)
                withContext(Dispatchers.Main) {
                    viewState.showList(position.first)
                }
            }
        }
    }

    private fun calculatePosition(holidays: List<Holiday>): Pair<Int, Holiday> {
        for ((index, holiday) in holidays.withIndex()) {
            if (holiday.monthWith0 >= time.monthWith0 && holiday.day >= time.dayOfMonth) {
                return index to holiday
            }
        }
        return 0 to Holiday()
    }

    override fun viewIsCreated() {
        GlobalScope.launch(Dispatchers.Main) {
            job?.join()
        }
    }
}