package com.artmaster.android.orthodoxcalendar.ui.tile_month.impl

import com.artmaster.android.orthodoxcalendar.domain.Filter
import java.util.*

interface ContractTileMonthPresenter {
    suspend fun viewIsReady(year: Int, month: Int, filters: ArrayList<Filter>)
    fun viewIsPaused()
    fun viewIsResumed()
    fun viewIsCreated()
}