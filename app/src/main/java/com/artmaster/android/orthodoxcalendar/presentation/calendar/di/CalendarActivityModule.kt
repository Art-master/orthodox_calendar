package com.artmaster.android.orthodoxcalendar.presentation.calendar.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.presentation.calendar.mvp.CalendarListContract
import com.artmaster.android.orthodoxcalendar.presentation.calendar.mvp.CalendarListDataModel
import com.artmaster.android.orthodoxcalendar.presentation.calendar.mvp.CalendarListPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [CalendarListBuilderModule::class])
open class CalendarActivityModule {
    @CalendarActivityScope
    @Provides
    fun provideModel(database: AppDatabase): CalendarListContract.Model {
        return CalendarListDataModel(database)
    }

    @CalendarActivityScope
    @Provides
    fun providePresenter(context: Context): CalendarListContract.Presenter {
        return CalendarListPresenter(context)
    }

}