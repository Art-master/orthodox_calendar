package com.artmaster.android.orthodoxcalendar.ui.calendar.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.ui.calendar.*
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import dagger.Module
import dagger.Provides

@Module
open class CalendarListBuilderModule {
    @FragmentScope
    @Provides
    fun providePageListConfig(): ListViewContract.Config {
        return PageConfig
    }

    @FragmentScope
    @Provides
    fun provideDataSource(context: Context): ListViewContract.DataSource<HolidayEntity> {
        return HolidayDataSource(context)
    }

    @FragmentScope
    @Provides
    fun providePagedList(dataSource: ListViewContract.DataSource<HolidayEntity>,
                         config: ListViewContract.Config): ListViewContract.PagedList<HolidayEntity> {
        return PagedList(dataSource, config)
    }

    @FragmentScope
    @Provides
    fun provideDiffUtilCallback(dataSource: ListViewContract.DataSource<HolidayEntity>):
            ListViewContract.CallBack<HolidayEntity> {
        return HolidayDiffUtilCallback(dataSource.getOldData(), dataSource.getNewData())
    }

    @FragmentScope
    @Provides
    fun provideAdapter(context: Context, diffCallback: ListViewContract.CallBack<HolidayEntity>,
                       list: ListViewContract.PagedList<HolidayEntity>): ListViewContract.Adapter {
        val adapter = HolidaysAdapter(context, diffCallback)
        adapter.submitList(list.get())
        return adapter
    }
}