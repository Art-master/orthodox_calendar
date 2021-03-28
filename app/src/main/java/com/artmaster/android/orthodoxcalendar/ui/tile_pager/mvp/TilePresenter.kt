package com.artmaster.android.orthodoxcalendar.ui.tile_pager.mvp

import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTilePresenter
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTileView
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class TilePresenter : MvpPresenter<ContractTileView>(), ContractTilePresenter {

    override fun viewIsReady() {

    }

    override fun viewIsCreated() {
        viewState.setPageAdapter()
        viewState.initSpinner()
    }

    override fun viewIsPaused() {}

    override fun viewIsResumed() {}
}