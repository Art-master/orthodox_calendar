package com.artmaster.android.orthodoxcalendar.ui.calendar_list.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter.*
import dagger.Module
import dagger.Provides

@Module
open class CalendarListBuilderModule {
    @FragmentScope
    @Provides
    fun providePageListConfig(): ListViewDiffContract.Config {
        return PageConfig
    }

    @FragmentScope
    @Provides
    fun provideDataSource(context: Context): ListViewDiffContract.DataSource<Holiday> {
        return HolidayDataSource(context)
    }

    @FragmentScope
    @Provides
    fun providePagedList(dataSource: ListViewDiffContract.DataSource<Holiday>,
                         config: ListViewDiffContract.Config): ListViewDiffContract.PagedList<Holiday> {
        return PagedList(dataSource, config, 0)
    }

    @FragmentScope
    @Provides
    fun provideDiffUtilCallback(dataSource: ListViewDiffContract.DataSource<Holiday>):
            ListViewDiffContract.CallBack<Holiday> {
        return HolidayDiffUtilCallback(dataSource.getOldData(), dataSource.getNewData())
    }

    @FragmentScope
    @Provides
    fun provideAdapter(context: Context, diffCallback: ListViewDiffContract.CallBack<Holiday>,
                       list: ListViewDiffContract.PagedList<Holiday>): ListViewDiffContract.Adapter {
        val adapter = HolidaysAdapter(context, diffCallback, ArrayList())
        adapter.submitList(list.get())
        return adapter
    }
}