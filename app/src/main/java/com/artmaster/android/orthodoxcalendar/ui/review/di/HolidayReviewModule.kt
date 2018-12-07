package com.artmaster.android.orthodoxcalendar.ui.review.di

import android.content.Context
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences
import com.artmaster.android.orthodoxcalendar.ui.review.impl.HolidayReviewContract
import com.artmaster.android.orthodoxcalendar.ui.review.mvp.HolidayReviewPresenter
import dagger.Module
import dagger.Provides

@Module
class HolidayReviewModule {

    @Provides
    @HolidayFragmentReviewScope
    fun providePresenter(context: Context, preferences: AppPreferences): HolidayReviewContract.Presenter {
        return HolidayReviewPresenter(context, preferences)
    }

}
