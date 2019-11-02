package com.artmaster.android.orthodoxcalendar.ui.review

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.impl.AppDataProvider
import dagger.android.AndroidInjection
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.holiday_review_pager.*
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.review.mvp.HolidayFragment
import dagger.android.AndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import dagger.android.DispatchingAndroidInjector


class HolidayViewPagerActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    @Inject
    lateinit var dataProvider: AppDataProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.holiday_review_pager)

        Single.fromCallable { dataProvider.getData(2018) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { data -> setPageAdapter(data) },
                        onError = { it.printStackTrace() })

    }

    private fun setPageAdapter(data: List<HolidayEntity>) {
        holidayPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {

            override fun getItem(p0: Int): Fragment {
                val holiday = data[p0]
                return HolidayFragment.newInstance(holiday)
            }

            override fun getCount(): Int = data.size
        }
        setCurrentItem(data)
    }

    private fun setCurrentItem(data: List<HolidayEntity>) {
        val uuid = intent.getSerializableExtra(Constants.Keys.HOLIDAY_ID.name)

        for ((index, holiday) in data.withIndex()) {
            if (holiday.uuid == uuid) {
                holidayPager.currentItem = index
                break
            }
        }
    }
}
