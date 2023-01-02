package com.artmaster.android.orthodoxcalendar.data.di.modules

import android.content.Context
import com.artmaster.android.orthodoxcalendar.api.AppFileParser
import com.artmaster.android.orthodoxcalendar.api.AppPreferences
import com.artmaster.android.orthodoxcalendar.api.RepositoryConnector
import com.artmaster.android.orthodoxcalendar.data.repository.CalendarPreferences
import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.data.repository.FileParser
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class, ContextModule::class])
open class RepositoryModule {

    @Singleton
    @Provides
    fun provideData(): RepositoryConnector {
        return DataProvider()
    }

    @Singleton
    @Provides
    fun providePreference(context: Context): AppPreferences {
        return CalendarPreferences(context)
    }

    @Singleton
    @Provides
    fun provideFileParser(context: Context): AppFileParser {
        return FileParser(context)
    }

}