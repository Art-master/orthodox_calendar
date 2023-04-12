package com.artmaster.android.orthodoxcalendar.data.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(private val context: Context) {
    @Singleton
    @Provides
    fun context(): Context {
        return context
    }
}