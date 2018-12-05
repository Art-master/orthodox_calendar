package com.artmaster.android.orthodoxcalendar.ui.init.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.data.repository.CalendarPreferences
import com.artmaster.android.orthodoxcalendar.data.repository.FileParser
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.impl.AppFileParser
import com.artmaster.android.orthodoxcalendar.ui.init.mvp.InitAppContract
import com.artmaster.android.orthodoxcalendar.ui.init.mvp.InitAppModel
import com.artmaster.android.orthodoxcalendar.ui.init.mvp.InitAppPresenter
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