package com.artmaster.android.orthodoxcalendar.ui.calendar_list.di


import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.ListHolidayPager
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.AppInfoView
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.AppSettingView
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.info.FragmentAppInfo
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.settings.FragmentSettingsApp
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.impl.CalendarListContractModel
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTileView
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.mvp.CalendarTileFragment
import dagger.Module
import dagger.Provides

@Module(includes = [FragmentInjectorModule::class])
class CalendarActivityModule {
    @CalendarActivityScope
    @Provides
    fun provideModel(): CalendarListContractModel {
        return DataProvider()
    }

    @CalendarActivityScope
    @Provides
    fun provideListHolidaysFragment(): ListViewDiffContract.ViewListPager {
        return ListHolidayPager()
    }

    @CalendarActivityScope
    @Provides
    fun provideTileHolidaysFragment(): ContractTileView {
        return CalendarTileFragment()
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