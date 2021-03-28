package com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl

import moxy.MvpView

interface ContractTileView : MvpView {
    fun setPageAdapter()
    fun initSpinner()
}