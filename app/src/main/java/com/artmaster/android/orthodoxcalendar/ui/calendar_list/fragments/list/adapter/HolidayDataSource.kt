package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter

import android.content.Context
import androidx.paging.PositionalDataSource
import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class HolidayDataSource(val context: Context, val year: Int = Time().year)
    : PositionalDataSource<Holiday>(), ListViewDiffContract.DataSource<Holiday> {

    private var mOldData: List<Holiday> = emptyList()
    private var mNewData: List<Holiday> = emptyList()

    private val dataProvider = DataProvider()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Holiday>) {
        Single.fromCallable { getData(params.startPosition, params.loadSize) }
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { data -> callback.onResult(data) },
                        onError = { it.printStackTrace() })
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Holiday>) {
        Single.fromCallable { getData(params.requestedStartPosition, params.requestedLoadSize) }
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { data -> callback.onResult(data, params.requestedStartPosition) },
                        onError = { it.printStackTrace() })
    }

    private fun getData(start: Int, size: Int): List<Holiday> {
        mOldData = mNewData
        mNewData = dataProvider.getDataSequence(start, size, year)
        return mNewData
    }

    override fun getOldData() = mOldData

    override fun getNewData() = mNewData
}