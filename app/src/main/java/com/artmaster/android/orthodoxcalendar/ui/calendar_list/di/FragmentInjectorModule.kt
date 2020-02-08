package com.artmaster.android.orthodoxcalendar.ui.calendar_list.di

import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.ListHolidayPager
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentInjectorModule {

    @FragmentScope
    @ContributesAndroidInjector
    fun providePageListConfig() : ListHolidayPager
}