package com.artmaster.android.orthodoxcalendar.ui.tile_pager.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTilePresenter
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTileView

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