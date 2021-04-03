package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthPresenter
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.*

@InjectViewState
class TileMonthPresenter : MvpPresenter<ContractTileMonthView>(), ContractTileMonthPresenter {

    private val time = Time()

    private var isViewCreated = false

    override suspend fun viewIsReady(year: Int, month: Int) {
        time.calendar.set(year, month, 1)

        getHolidays(year, month)
        viewData(time)
    }

    private suspend fun getHolidays(year: Int, month: Int): List<Day> {
        return withContext(Dispatchers.IO) {
            val days = DataProvider().getMonthDays(month, year)
            prepareView(days, time)
            return@withContext days
        }
    }

    override fun viewIsCreated() {
        isViewCreated = true
    }

    private fun prepareView(days: List<Day>, time: Time) {
        viewState.prepareDaysOfWeekRows(1..7)
        val numDays = time.daysInMonth
        for (index in 1..numDays) {
            time.calendar.set(Calendar.DAY_OF_MONTH, index)
            val dayOfWeek = time.dayOfWeek
            val week = time.calendar.get(Calendar.WEEK_OF_MONTH)
            viewState.prepareDayOfMonth(dayOfWeek, week, days[index - 1])
        }
    }

    private fun viewData(time: Time) {
        viewState.clearView()
        viewState.drawView()
        //viewState.setFocus(time.month - 1)
    }

    override fun viewIsPaused() {
    }

    override fun viewIsResumed() {}
}