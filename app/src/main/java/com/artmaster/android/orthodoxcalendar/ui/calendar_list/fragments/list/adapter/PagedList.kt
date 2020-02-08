package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter

import android.arch.paging.DataSource
import android.arch.paging.PagedList
import android.os.Handler
import android.os.Looper
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import java.util.concurrent.Executors

open class PagedList(private val dataSource: ListViewDiffContract.DataSource<HolidayEntity>,
                     private val pagedConfig: ListViewDiffContract.Config,
                     private val position: Int) : ListViewDiffContract.PagedList<HolidayEntity> {

    override fun get(): PagedList<HolidayEntity> {
        return PagedList.Builder(dataSource as DataSource<Int, HolidayEntity>, pagedConfig.get())
                .setInitialKey(position)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setNotifyExecutor { runnable -> Handler(Looper.getMainLooper()).post(runnable) }
                .build()
    }
}