package com.artmaster.android.orthodoxcalendar.ui.calendar.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.FragmentAppInfo
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.FragmentSettingsApp
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.HolidayListFragment
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.impl.AppInfoView
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.impl.AppSettingView
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import com.artmaster.android.orthodoxcalendar.ui.calendar.mvp.CalendarListContract
import com.artmaster.android.orthodoxcalendar.ui.calendar.mvp.CalendarListDataProvider
import com.artmaster.android.orthodoxcalendar.ui.calendar.mvp.CalendarListPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [FragmentInjectorModule::class])
class CalendarActivityModule {
    @CalendarActivityScope
    @Provides
    fun provideModel(database: AppDatabase): CalendarListContract.Model {
        return CalendarListDataProvider(database)
    }

    @CalendarActivityScope
    @Provides
    fun providePresenter(context: Context): CalendarListContract.Presenter {
        return CalendarListPresenter(context)
    }

    @CalendarActivityScope
    @Provides
    fun provideListHolidaysFragment(): ListViewContract.ViewList {
        return HolidayListFragment()
    }

    @CalendarActivityScope
    @Provides
    fun provideAppInfoFragment(): AppInfoView {
        return FragmentAppInfo()
    }

    @CalendarActivityScope
    @Provides
    fun provideAppSettingsFragment(): AppSettingView {
        return FragmentSettingsApp()
    }
}