package com.artmaster.android.orthodoxcalendar.data.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.api.AppDatabase
import com.artmaster.android.orthodoxcalendar.api.AppFileParser
import com.artmaster.android.orthodoxcalendar.api.AppPreferences
import com.artmaster.android.orthodoxcalendar.api.RepositoryConnector
import com.artmaster.android.orthodoxcalendar.data.di.modules.ContextModule
import com.artmaster.android.orthodoxcalendar.data.di.modules.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, RepositoryModule::class])
interface AppComponent {
    fun getContext(): Context
    fun getPreferences(): AppPreferences
    fun getRepository(): RepositoryConnector
    fun getFileParser(): AppFileParser
    fun getDatabase(): AppDatabase
}