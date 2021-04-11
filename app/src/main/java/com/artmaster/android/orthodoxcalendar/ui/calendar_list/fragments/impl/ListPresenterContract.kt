package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl

import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Time

interface ListPresenterContract {
    suspend fun viewIsReady(time: Time, filters: List<Filter>)
}
