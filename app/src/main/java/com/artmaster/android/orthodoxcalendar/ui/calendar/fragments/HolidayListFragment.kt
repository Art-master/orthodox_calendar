package com.artmaster.android.orthodoxcalendar.ui.calendar.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import com.artmaster.android.orthodoxcalendar.R
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.calendar_list_fragment.*
import javax.inject.Inject

class HolidayListFragment : Fragment(), ListViewContract.ViewList {
    @Inject
    lateinit var recyclerAdapter: ListViewContract.Adapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.calendar_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerAdapter as? RecyclerView.Adapter<*>
        super.onViewCreated(view, savedInstanceState)
    }
}