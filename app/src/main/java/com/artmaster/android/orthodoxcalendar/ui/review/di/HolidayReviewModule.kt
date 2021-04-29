package com.artmaster.android.orthodoxcalendar.ui.review.di

import com.artmaster.android.orthodoxcalendar.ui.review.impl.HolidayReviewContract
import com.artmaster.android.orthodoxcalendar.ui.review.mvp.HolidayReviewPresenter
import dagger.Module
import dagger.Provides

@Module
class HolidayReviewModule {

    @Provides
    @HolidayFragmentReviewScope
    fun providePresenter(): HolidayReviewContract.Presenter {
        return HolidayReviewPresenter()
    }

}
