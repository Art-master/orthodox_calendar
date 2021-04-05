package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter

import android.content.Context
import androidx.paging.PositionalDataSource
import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HolidayDataSource(val context: Context, val year: Int = Time().year)
    : PositionalDataSource<Holiday>(), ListViewDiffContract.DataSource<Holiday> {

    private var mOldData: List<Holiday> = emptyList()
    private var mNewData: List<Holiday> = emptyList()

    private val dataProvider = DataProvider()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Holiday>) {
        GlobalScope.launch {
            val holidays = getData(params.startPosition, params.loadSize)
            callback.onResult(holidays)
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Holiday>) {
        GlobalScope.launch {
            val holidays = getData(params.requestedStartPosition, params.requestedLoadSize)
            callback.onResult(holidays, params.requestedStartPosition)
        }
    }

    private suspend fun getData(start: Int, size: Int): List<Holiday> {
        return withContext(Dispatchers.IO) {
            mOldData = mNewData
            mNewData = dataProvider.getDataSequence(start, size, year)
            mNewData
        }
    }

    override fun getOldData() = mOldData

    override fun getNewData() = mNewData
}