package com.artmaster.android.orthodoxcalendar

import android.app.Application
import com.artmaster.android.orthodoxcalendar.data.di.AppComponent
import com.artmaster.android.orthodoxcalendar.data.di.DaggerAndroidAppComponent
import com.artmaster.android.orthodoxcalendar.data.di.DaggerAppComponent
import com.artmaster.android.orthodoxcalendar.data.di.modules.ContextModule
import com.squareup.leakcanary.LeakCanary
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

        DaggerAndroidAppComponent
                .builder()
                .context(this)
                .build()
                .inject(this)
        appComponent = DaggerAppComponent
                .builder()
                .contextModule(ContextModule(applicationContext))
                .build()

    }

    override fun androidInjector() = dispatchingAndroidInjector
}