package com.artmaster.android.orthodoxcalendar.ui.calendar.di

import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import dagger.Module

@Module(includes = [CalendarListBuilderModule::class])
interface ListAdapterModule {

    @FragmentScope
    fun provideListViewFragment(): ListViewContract.Adapter
}
