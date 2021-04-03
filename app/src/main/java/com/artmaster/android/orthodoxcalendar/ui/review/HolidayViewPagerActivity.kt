package com.artmaster.android.orthodoxcalendar.ui.review

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.databinding.HolidayReviewPagerBinding
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.impl.AppDataProvider
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.mvp.CalendarListActivity
import com.artmaster.android.orthodoxcalendar.ui.review.mvp.HolidayFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class HolidayViewPagerActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return fragmentInjector
    }

    @Inject
    lateinit var dataProvider: AppDataProvider

    private lateinit var currentHoliday: Holiday

    private var _binding: HolidayReviewPagerBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun getIntent(context: Context, holiday: Holiday): Intent {
            val intent = Intent(context, HolidayViewPagerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(Constants.Keys.HOLIDAY.value, holiday)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        _binding = HolidayReviewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentHoliday = getCurrentHoliday()

        loadHolidays()
    }

    fun loadHolidays() {
        lifecycleScope.launchWhenCreated {
            val holidays = ArrayList<Holiday>()
            withContext(Dispatchers.IO) {
                holidays.addAll(dataProvider.getData(currentHoliday.year))
            }
            setPageAdapter(holidays)
        }
    }

    private fun getCurrentHoliday() = intent.getParcelableExtra<Holiday>(Constants.Keys.HOLIDAY.value)

    private fun setPageAdapter(data: List<Holiday>) {
        binding.holidayPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = data.size

            override fun createFragment(position: Int): Fragment {
                val holiday = data[position]
                return HolidayFragment.newInstance(holiday)
            }
        }

        setCurrentItem(data)
    }

    private fun setCurrentItem(data: List<Holiday>) {
        for ((index, holiday) in data.withIndex()) {
            if (holiday.id == currentHoliday.id) {
                binding.holidayPager.currentItem = index
            }
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
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
