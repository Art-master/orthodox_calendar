package com.artmaster.android.orthodoxcalendar.impl.mvp

abstract class AbstractAppPresenter<T : AppView> : AppPresenter<T> {
    var appView: T? = null
    override fun attachView(mvpView: T) {
        appView = mvpView
    }

    fun getView(): T {
        return appView!!
    }

    override fun detachView() {
        appView = null
    }

    override fun isViewAttached(): Boolean {
        return appView != null
    }

    override fun onDestroy() {}
}