package com.artmaster.android.orthodoxcalendar.ui.tile_pager.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTilePresenter
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTileView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@InjectViewState
class TilePresenter : MvpPresenter<ContractTileView>(), ContractTilePresenter {

    private val dispose = CompositeDisposable()

    private lateinit var single: Disposable

    override fun viewIsReady() {

    }

    override fun viewIsCreated() {
        viewState.setPageAdapter()
    }

    override fun viewIsPaused() {}

    override fun viewIsResumed() {}

    override fun onDestroy() {
        super.onDestroy()
        dispose.clear()
    }
}