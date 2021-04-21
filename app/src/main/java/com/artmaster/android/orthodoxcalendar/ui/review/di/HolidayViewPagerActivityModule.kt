package com.artmaster.android.orthodoxcalendar.ui.review.di

import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.impl.RepositoryConnector
import dagger.Module
import dagger.Provides

@Module(includes = [HolidayReviewFragmentInjectorModule::class])
class HolidayViewPagerActivityModule {
    @HolidayViewPagerActivityScope
    @Provides
    fun provideData(): RepositoryConnector {
        return DataProvider()
    }
}