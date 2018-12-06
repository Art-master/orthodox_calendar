package com.artmaster.android.orthodoxcalendar.ui.review.di

import com.artmaster.android.orthodoxcalendar.ui.review.mvp.HolidayFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface HolidayReviewFragmentInjectorModule {

    @HolidayFragmentReviewScope
    @ContributesAndroidInjector(modules = [HolidayReviewModule::class])
    fun provideListViewFragment(): HolidayFragment
}