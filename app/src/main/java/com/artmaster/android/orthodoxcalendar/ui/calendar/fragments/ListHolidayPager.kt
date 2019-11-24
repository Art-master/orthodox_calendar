package com.artmaster.android.orthodoxcalendar.ui.calendar.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.holiday_list_pager.*
import android.support.v4.view.ViewPager.OnPageChangeListener
import com.artmaster.android.orthodoxcalendar.domain.Time2


class ListHolidayPager : Fragment(), ListViewContract.ViewListPager {

    private lateinit var adapter : FragmentStatePagerAdapter

    private var changedCallback : ((Int) -> Unit)? = null

    private val years = getYears()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = getAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.holiday_list_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPageAdapter()
    }

    private fun setPageAdapter() {
        if(holidayListPager.adapter != null) return
        holidayListPager.adapter =  adapter
        setChangePageListener()
    }

    override fun setPage(numPage: Int) {
        if(holidayListPager == null) return
        holidayListPager.currentItem = numPage
    }
    private fun getYears(): ArrayList<Int> {
        val size = Constants.HolidayList.PAGE_SIZE.value
        val initYear = Time2().year - size/2
        val years = ArrayList<Int>(size)
        for (element in initYear..initYear+size){
            years.add(element)
        }
        return years
    }

    private fun getAdapter(): FragmentStatePagerAdapter {
        return object :FragmentStatePagerAdapter(childFragmentManager) {

            override fun getItem(p0: Int): Fragment {
                val fragment = HolidayListFragment()
                val bundle = Bundle().apply {
                    putInt(Constants.Keys.YEAR.value, years[p0])
                }
                fragment.arguments = bundle
                return fragment
            }

            override fun getCount(): Int = Constants.HolidayList.PAGE_SIZE.value
        }
    }

    override fun setYear(year: String) {

    }

    override fun onChangePageListener(body: (Int) -> Unit) {
        changedCallback = body
    }

    private fun setChangePageListener() {
        holidayListPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
               changedCallback?.let {
                   it(position)
                   val fr = getAdapter().getItem(position)
                   //val holiday = (fr as HolidayListFragment).getCurrentElement(position)
               }
            }
        })
    }
}