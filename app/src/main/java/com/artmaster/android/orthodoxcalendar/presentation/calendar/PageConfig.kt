package com.artmaster.android.orthodoxcalendar.presentation.calendar

import android.arch.paging.PagedList
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.presentation.calendar.impl.ListViewContract

object PageConfig : ListViewContract.Config {
    override fun get(): PagedList.Config {
        return PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(Constants.HolidayList.PAGE_SIZE.value)
                .build()
    }

}