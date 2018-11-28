package com.artmaster.android.orthodoxcalendar.presentation.init.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.data.repository.CalendarPreferences
import com.artmaster.android.orthodoxcalendar.data.repository.FileParser
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.impl.AppFileParser
import com.artmaster.android.orthodoxcalendar.presentation.init.mvp.InitAppContract
import com.artmaster.android.orthodoxcalendar.presentation.init.mvp.InitAppModel
import com.artmaster.android.orthodoxcalendar.presentation.init.mvp.InitAppPresenter
import dagger.Module
import dagger.Provides

@Module
open class InitAppActivityModule {

    @InitAppActivityScope
    @Provides
    fun provideParser(context: Context): AppFileParser {
        return FileParser(context)
    }

    @InitAppActivityScope
    @Provides
    fun provideModel(database: AppDatabase, parser: AppFileParser): InitAppContract.Model {
        return InitAppModel(database, parser)
    }

    @InitAppActivityScope
    @Provides
    fun providePresenter(preferences: CalendarPreferences, model: InitAppContract.Model): InitAppContract.Presenter {
        return InitAppPresenter(preferences, model)
    }
}