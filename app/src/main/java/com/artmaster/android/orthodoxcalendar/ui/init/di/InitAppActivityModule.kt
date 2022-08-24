package com.artmaster.android.orthodoxcalendar.ui.init.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.data.repository.FileParser
import com.artmaster.android.orthodoxcalendar.impl.AppFileParser
import dagger.Module
import dagger.Provides

@Module
open class InitAppActivityModule {

    @InitAppActivityScope
    @Provides
    fun provideParser(context: Context): AppFileParser {
        return FileParser(context)
    }
}