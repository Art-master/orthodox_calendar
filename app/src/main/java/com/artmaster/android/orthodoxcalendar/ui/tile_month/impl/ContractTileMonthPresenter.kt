package com.artmaster.android.orthodoxcalendar.ui.tile_month.impl

interface ContractTileMonthPresenter {
    suspend fun viewIsReady(year: Int, month: Int)
    fun viewIsPaused()
    fun viewIsResumed()
    fun viewIsCreated()
}