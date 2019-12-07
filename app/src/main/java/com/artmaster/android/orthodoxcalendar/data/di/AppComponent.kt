package com.artmaster.android.orthodoxcalendar.data.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.data.di.modules.ContextModule
import com.artmaster.android.orthodoxcalendar.data.di.modules.RepositoryModule
import com.artmaster.android.orthodoxcalendar.impl.AppDataProvider
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, RepositoryModule::class])
interface AppComponent {
    fun getContext(): Context
    //fun getPreferences(): AppPreferences
    fun getRepository(): AppDataProvider
    fun getDatabase(): AppDatabase
}