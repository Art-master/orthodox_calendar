package com.artmaster.android.orthodoxcalendar.ui.calendar.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.holiday_list_pager.*

class ListHolidayPager : Fragment(), ListViewContract.ViewListPager {

    private lateinit var adapter : FragmentStatePagerAdapter

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

    fun setPageAdapter() {
        if(holidayListPager.adapter != null) return
        holidayListPager.adapter =  adapter
    }

    private fun getAdapter(): FragmentStatePagerAdapter {
        return object :FragmentStatePagerAdapter(childFragmentManager) {

            override fun getItem(p0: Int): Fragment {
                val fragment = HolidayListFragment()
                return fragment
            }

            override fun getCount(): Int = 10 //month size
        }
    }
}