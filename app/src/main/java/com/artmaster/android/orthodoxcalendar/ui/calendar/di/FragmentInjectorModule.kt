package com.artmaster.android.orthodoxcalendar.ui.calendar.di

import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.ListHolidayPager
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentInjectorModule {

    @FragmentScope
    @ContributesAndroidInjector
    fun providePageListConfig() : ListHolidayPager
}