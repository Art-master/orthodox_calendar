package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.databinding.HolidayListPagerBinding
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.CalendarUpdateContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.HolidayListFragment
import dagger.android.support.AndroidSupportInjection


class ListHolidayPager : Fragment(), ListViewDiffContract.ViewListPager, CalendarUpdateContract {

    private lateinit var adapter: FragmentStatePagerAdapter

    private var changedCallback: ((Int) -> Unit)? = null

    private val years = getYears()

    private var _binding: HolidayListPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = getAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        AndroidSupportInjection.inject(this)
        _binding = HolidayListPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPageAdapter()
    }

    override fun onResume() {
        super.onResume()
        binding.holidayListPager.currentItem = getPosition()
    }

    private fun getPosition(): Int {
        return years.indexOf(getYear())
    }

    private fun setPageAdapter() {
        if (binding.holidayListPager.adapter != null) return
        binding.holidayListPager.adapter = adapter
        binding.holidayListPager.currentItem = getPosition()
        setChangePageListener()
    }

    private fun getYears(): ArrayList<Int> {
        val size = Constants.HolidayList.PAGE_SIZE.value
        val firstYear = Time().year - size / 2
        val years = ArrayList<Int>(size)
        for (element in firstYear until firstYear + size) {
            years.add(element)
        }
        return years
    }

    private fun getAdapter(): FragmentStatePagerAdapter {
        return object : FragmentStatePagerAdapter(childFragmentManager) {

            override fun getItem(p0: Int): Fragment {
                val fragment = HolidayListFragment()

                val bundle = Bundle()
                bundle.putInt(Constants.Keys.YEAR.value, years[p0])
                fragment.arguments = bundle

                return fragment
            }

            override fun getCount(): Int = Constants.HolidayList.PAGE_SIZE.value
        }
    }

    override fun onChangePageListener(body: (Int) -> Unit) {
        changedCallback = body
    }

    private fun setChangePageListener() {
        binding.holidayListPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                changedCallback?.let {
                    it(position)
                }
            }
        })
    }

    private fun getYear() = requireArguments().getInt(Constants.Keys.YEAR.value, Time().year)

    override fun updateYear() {
        val years = getYears()
        val pos = years.indexOf(getYear())

        binding.holidayListPager.currentItem = pos
    }

    override fun updateMonth() {

    }

    override fun updateDay() {

    }
}