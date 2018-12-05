package com.artmaster.android.orthodoxcalendar.ui.calendar.di

import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.HolidayListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentInjectorModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [CalendarListBuilderModule::class])
    fun provideListViewFragment(): HolidayListFragment
}