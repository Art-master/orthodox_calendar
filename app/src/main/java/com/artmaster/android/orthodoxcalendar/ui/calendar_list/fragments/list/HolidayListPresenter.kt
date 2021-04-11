package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list

import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListPresenterContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class HolidayListPresenter : MvpPresenter<ListViewContract>(), ListPresenterContract {

    private var time = Time()

    override suspend fun viewIsReady(time: Time, filters: List<Filter>) {
        this.time = time
        val holidays = getHolidays(time.year, filters)
        viewData(holidays)
    }

    private suspend fun getHolidays(year: Int, filters: List<Filter>): Pair<Int, Holiday> {
        return withContext(Dispatchers.IO) {
            val holidays = DataProvider().getData(year, filters)
            return@withContext calculatePosition(holidays)
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

    private fun viewData(pos: Pair<Int, Holiday>) {
        viewState.prepareAdapter(pos.first, pos.second)
    }
}