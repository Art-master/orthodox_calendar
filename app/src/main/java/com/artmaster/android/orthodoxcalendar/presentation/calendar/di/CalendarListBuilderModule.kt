package com.artmaster.android.orthodoxcalendar.presentation.calendar.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.presentation.calendar.*
import com.artmaster.android.orthodoxcalendar.presentation.calendar.impl.ListViewContract
import dagger.Module
import dagger.Provides

@Module
open class CalendarListBuilderModule {

    @CalendarActivityScope
    @Provides
    fun providePageListConfig(): ListViewContract.Config {
        return PageConfig
    }

    @CalendarActivityScope
    @Provides
    fun provideDataSource(context: Context): ListViewContract.DataSource<HolidayEntity> {
        return HolidayDataSource(context)
    }

    @CalendarActivityScope
    @Provides
    fun providePagedList(dataSource: ListViewContract.DataSource<HolidayEntity>,
                         config: ListViewContract.Config): ListViewContract.PagedList<HolidayEntity> {
        return PagedList(dataSource, config)
    }

    @CalendarActivityScope
    @Provides
    fun provideDiffUtilCallback(dataSource: ListViewContract.DataSource<HolidayEntity>):
            ListViewContract.CallBack<HolidayEntity> {
        return HolidayDiffUtilCallback(dataSource.getOldData(), dataSource.getNewData())
    }

    @CalendarActivityScope
    @Provides
    fun provideAdapter(diffCallback: ListViewContract.CallBack<HolidayEntity>,
                       list: ListViewContract.PagedList<HolidayEntity>): ListViewContract.Adapter {
        val adapter = HolidaysAdapter(diffCallback)
        adapter.submitList(list.get())
        return adapter
    }
}