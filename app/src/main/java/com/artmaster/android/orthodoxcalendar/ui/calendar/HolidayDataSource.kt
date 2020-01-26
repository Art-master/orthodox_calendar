package com.artmaster.android.orthodoxcalendar.ui.calendar

import android.arch.paging.PositionalDataSource
import android.content.Context
import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class HolidayDataSource(val context: Context, val year: Int = Time().year)
    : PositionalDataSource<HolidayEntity>(), ListViewContract.DataSource<HolidayEntity> {

    private var mOldData: List<HolidayEntity> = emptyList()
    private var mNewData: List<HolidayEntity> = emptyList()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<HolidayEntity>) {
        Single.fromCallable { getData(params.startPosition, params.loadSize) }
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { data -> callback.onResult(data) },
                        onError = { it.printStackTrace() })
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<HolidayEntity>) {
        Single.fromCallable { getData(params.requestedStartPosition, params.requestedLoadSize) }
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { data -> callback.onResult(data, 0) },
                        onError = { it.printStackTrace() })
    }

    private fun getData(start: Int, size: Int): List<HolidayEntity> {
        mOldData = mNewData
        mNewData = DataProvider().getDataSequence(start, size, year)
        return mNewData
    }

    override fun getOldData() = mOldData

    override fun getNewData() = mNewData
}