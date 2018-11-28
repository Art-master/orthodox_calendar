package com.artmaster.android.orthodoxcalendar.impl.mvp

interface AppPresenter<T : AppView> {
    fun attachView(mvpView: T)
    fun viewIsReady()
    fun isViewAttached(): Boolean
    fun detachView()
    fun destroy()
}