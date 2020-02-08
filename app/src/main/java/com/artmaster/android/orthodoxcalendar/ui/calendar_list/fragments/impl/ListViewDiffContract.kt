package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl

import android.arch.paging.PagedList as pgdList
import android.arch.paging.PagedList.Config as cfg

interface ListViewDiffContract {
    interface Config {
        fun get(): cfg
    }

    interface DataSource<T> {
        fun getOldData(): List<T>
        fun getNewData(): List<T>
    }

    interface PagedList<T> {
        fun get(): pgdList<T>
    }

    interface CallBack<T>
    interface Adapter

    interface ViewList
    interface ViewListPager{
        fun onChangePageListener(body: (Int) -> Unit)
    }
}