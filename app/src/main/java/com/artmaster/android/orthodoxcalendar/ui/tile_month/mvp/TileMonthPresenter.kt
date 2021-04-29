package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.SharedTime
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthPresenter
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthView
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.*

@InjectViewState
class TileMonthPresenter : MvpPresenter<ContractTileMonthView>(), ContractTileMonthPresenter {

    private val currentTime = Time()

    private var job: Job? = null

    override suspend fun viewIsReady(time: SharedTime, filters: ArrayList<Filter>) {
        currentTime.calendar.set(time.year, time.month, 1)

        job = GlobalScope.launch(Dispatchers.Unconfined) {
            withContext(Dispatchers.IO) {
                val days = DataProvider().getMonthDays(time.month, time.year, filters)
                delay(1000)
                withContext(Dispatchers.Main) {
                    prepareView(days, currentTime)
                    viewData()
                }
            }
        }
    }

    override fun viewIsCreated() {
        GlobalScope.launch(Dispatchers.Main) {
            job?.join()
        }
    }

    private fun prepareView(days: List<Day>, time: Time) {
        viewState.prepareDaysOfWeekRows(1..7)
        viewState.prepareMonthsDays(days, time)
    }

    private fun viewData() {
        viewState.clearView()
        viewState.drawView()
        viewState.setFocus()
    }

    override fun viewIsPaused() {
    }

    override fun viewIsResumed() {}
}