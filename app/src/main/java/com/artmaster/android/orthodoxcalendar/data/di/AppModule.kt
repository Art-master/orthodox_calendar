package com.artmaster.android.orthodoxcalendar.data.di

import com.artmaster.android.orthodoxcalendar.data.di.modules.DatabaseModule
import com.artmaster.android.orthodoxcalendar.data.repository.CalendarPreferences
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences
import dagger.Binds
import dagger.Module
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

}