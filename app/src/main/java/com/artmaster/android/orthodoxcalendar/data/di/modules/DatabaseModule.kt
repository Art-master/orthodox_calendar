package com.artmaster.android.orthodoxcalendar.data.di.modules

import com.artmaster.android.orthodoxcalendar.data.repository.HolidayDatabase
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class DatabaseModule {

    @Singleton
    @Provides
    fun getAppDatabase(): AppDatabase {
        return HolidayDatabase
    }
}