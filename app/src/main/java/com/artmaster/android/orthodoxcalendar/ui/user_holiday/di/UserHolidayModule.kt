package com.artmaster.android.orthodoxcalendar.ui.user_holiday.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.impl.ContractUserHolidayPresenter
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.mvp.UserHolidayPresenter
import dagger.Module
import dagger.Provides

@Module
class UserHolidayModule {

    @Provides
    @UserHolidayScope
    fun providePresenter(context: Context): ContractUserHolidayPresenter {
        return UserHolidayPresenter()
    }

}
