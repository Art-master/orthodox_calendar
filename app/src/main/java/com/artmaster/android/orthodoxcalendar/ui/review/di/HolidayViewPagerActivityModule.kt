package com.artmaster.android.orthodoxcalendar.ui.review.di


import com.artmaster.android.orthodoxcalendar.impl.AppDataProvider
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.ui.calendar.mvp.CalendarListDataProvider
import dagger.Module
import dagger.Provides

@Module(includes = [HolidayReviewFragmentInjectorModule::class])
class HolidayViewPagerActivityModule {
    @HolidayViewPagerActivityScope
    @Provides
    fun provideData(database: AppDatabase): AppDataProvider {
        return CalendarListDataProvider(database)
    }
}