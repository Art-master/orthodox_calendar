package com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ContractTileView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setPageAdapter()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun initSpinner()
}