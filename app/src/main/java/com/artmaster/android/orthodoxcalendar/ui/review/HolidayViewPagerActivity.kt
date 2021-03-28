package com.artmaster.android.orthodoxcalendar.ui.review

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.impl.AppDataProvider
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.mvp.CalendarListActivity
import com.artmaster.android.orthodoxcalendar.ui.review.mvp.HolidayFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.holiday_review_pager.*
import javax.inject.Inject


class HolidayViewPagerActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = fragmentInjector

    @Inject
    lateinit var dataProvider: AppDataProvider

    private lateinit var currentHoliday: HolidayEntity

    companion object{
        fun getIntent(context: Context, holiday: HolidayEntity): Intent {
            val intent = Intent(context, HolidayViewPagerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(Constants.Keys.HOLIDAY.value, holiday)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.holiday_review_pager)

        currentHoliday = getCurrentHoliday()

        Single.fromCallable { dataProvider.getData(currentHoliday.year) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { data -> setPageAdapter(data) },
                        onError = { it.printStackTrace() })

    }

    private fun getCurrentHoliday() = intent.getParcelableExtra<HolidayEntity>(Constants.Keys.HOLIDAY.value)

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
            for ((index, holiday) in data.withIndex()){
                if(holiday.id == currentHoliday.id){
                    holidayPager.currentItem = index
                }
            }
        }

    override fun onBackPressed() {
        if(isTaskRoot){
            startActivity(getArgs())
        } else {
            super.onBackPressed()
        }
    }

    private fun getArgs() = Intent(applicationContext, CalendarListActivity::class.java).apply {
        putExtra(Constants.Keys.YEAR.value, Time().year)
        putExtra(Constants.Keys.MONTH.value, Time().monthWith0)
        putExtra(Constants.Keys.DAY.value, Time().dayOfMonth)
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    }
}
