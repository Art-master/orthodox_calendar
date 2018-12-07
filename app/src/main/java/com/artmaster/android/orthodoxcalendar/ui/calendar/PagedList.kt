package com.artmaster.android.orthodoxcalendar.ui.calendar

import android.arch.paging.DataSource
import android.arch.paging.PagedList
import android.os.Handler
import android.os.Looper
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import java.util.concurrent.Executors

open class PagedList(private val dataSource: ListViewContract.DataSource<HolidayEntity>,
                     private val pagedConfig: ListViewContract.Config) : ListViewContract.PagedList<HolidayEntity> {

    override fun get(): PagedList<HolidayEntity> {
        return PagedList.Builder(dataSource as DataSource<HolidayEntity, HolidayEntity>, pagedConfig.get())
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setNotifyExecutor { runnable -> Handler(Looper.getMainLooper()).post(runnable) }
                .build()
    }
}