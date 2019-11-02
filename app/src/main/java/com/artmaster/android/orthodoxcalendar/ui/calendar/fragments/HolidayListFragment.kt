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
import com.artmaster.android.orthodoxcalendar.ui.calendar.*
import kotlinx.android.synthetic.main.calendar_list_fragment.view.*

class HolidayListFragment : Fragment(), ListViewContract.ViewList {

    lateinit var recyclerAdapter: ListViewContract.Adapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_list_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerAdapter = getAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recyclerView!!.layoutManager = LinearLayoutManager(context)
        view.recyclerView.adapter = recyclerAdapter as? RecyclerView.Adapter<*>
    }

    private fun getAdapter() : ListViewContract.Adapter{
        val config = PageConfig
        val dataSource = HolidayDataSource(context!!)
        val list = PagedList(dataSource, config)
        val diffCallback = HolidayDiffUtilCallback(dataSource.getOldData(), dataSource.getNewData())
        val adapter = HolidaysAdapter(context!!, diffCallback)
        adapter.submitList(list.get())
        return adapter
    }
}