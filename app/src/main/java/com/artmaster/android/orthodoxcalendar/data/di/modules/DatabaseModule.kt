package com.artmaster.android.orthodoxcalendar.data.di.modules

import android.content.Context
import com.artmaster.android.orthodoxcalendar.data.repository.HolidayDatabase
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class DatabaseModule {

    @Singleton
    @Provides
    fun getAppDatabase(context: Context): AppDatabase {
        return HolidayDatabase.getAppDataBase(context)!!
    }
}