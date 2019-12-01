package com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl

import com.arellomobile.mvp.MvpView

interface ContractTileView : MvpView {
    fun setPageAdapter()
    fun initSpinner()
    fun upadteView()
}