package com.artmaster.android.orthodoxcalendar.ui.tile_month.impl

import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.SharedTime
import java.util.*

interface ContractTileMonthPresenter {
    suspend fun viewIsReady(time: SharedTime, filters: ArrayList<Filter>)
    fun viewIsPaused()
    fun viewIsResumed()
    fun viewIsCreated()
}