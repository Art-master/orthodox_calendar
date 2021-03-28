package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter

import androidx.paging.PagedList
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract

object PageConfig : ListViewDiffContract.Config {
    override fun get(): PagedList.Config {
        return PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(Constants.HolidayList.PAGE_SIZE.value)
                .build()
    }

}