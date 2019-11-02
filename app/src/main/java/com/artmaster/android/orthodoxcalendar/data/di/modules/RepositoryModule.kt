package com.artmaster.android.orthodoxcalendar.data.di.modules

import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.impl.AppDataProvider
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class])
open class RepositoryModule {

    @Singleton
    @Provides
    fun provideData(database : AppDatabase): AppDataProvider{
        return DataProvider(database)
    }

}