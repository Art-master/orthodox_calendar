package com.artmaster.android.orthodoxcalendar.data.di

import com.artmaster.android.orthodoxcalendar.data.di.modules.DatabaseModule
import com.artmaster.android.orthodoxcalendar.data.repository.CalendarPreferences
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.di.CalendarActivityModule
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.di.CalendarActivityScope
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.mvp.MainCalendarActivity
import com.artmaster.android.orthodoxcalendar.ui.init.InitAppActivity
import com.artmaster.android.orthodoxcalendar.ui.init.di.InitAppActivityModule
import com.artmaster.android.orthodoxcalendar.ui.init.di.InitAppActivityScope
import com.artmaster.android.orthodoxcalendar.ui.review.HolidayViewPagerActivity
import com.artmaster.android.orthodoxcalendar.ui.review.di.HolidayViewPagerActivityModule
import com.artmaster.android.orthodoxcalendar.ui.review.di.HolidayViewPagerActivityScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Basic application module
 */
@Module(includes = [AndroidSupportInjectionModule::class, DatabaseModule::class])
interface AppModule {
    @Binds
    @Singleton
    fun preferrences(preferences: CalendarPreferences): AppPreferences

    @InitAppActivityScope
    @ContributesAndroidInjector(modules = [InitAppActivityModule::class])
    fun loadingScreenActivityInjector(): InitAppActivity

    @CalendarActivityScope
    @ContributesAndroidInjector(modules = [CalendarActivityModule::class])
    fun calendarListActivityInjector(): MainCalendarActivity

    @HolidayViewPagerActivityScope
    @ContributesAndroidInjector(modules = [HolidayViewPagerActivityModule::class])
    fun holidayViewPagerActivityInjector(): HolidayViewPagerActivity
}