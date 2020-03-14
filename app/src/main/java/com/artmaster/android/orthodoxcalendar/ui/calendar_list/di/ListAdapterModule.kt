package com.artmaster.android.orthodoxcalendar.ui.calendar_list.di

import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import dagger.Module

@Module(includes = [CalendarListBuilderModule::class])
interface ListAdapterModule {

    @FragmentScope
    fun provideListViewFragment(): ListViewDiffContract.Adapter
}
