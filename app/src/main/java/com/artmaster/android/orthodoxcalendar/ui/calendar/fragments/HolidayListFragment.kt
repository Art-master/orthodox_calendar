package com.artmaster.android.orthodoxcalendar.ui.calendar.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar.*
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import kotlinx.android.synthetic.main.calendar_list_fragment.view.*


class HolidayListFragment : Fragment(), ListViewContract.ViewList {

    private lateinit var recyclerAdapter: ListViewContract.Adapter

    private lateinit var dataSource : HolidayDataSource

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
        val i = calculatePosition()
        view.recyclerView.scrollToPosition(calculatePosition() + 5)
    }

    private fun calculatePosition(): Int {
        return getLastPosition() ?: getInitPosition() ?: 0
    }

    private fun getLastPosition() = parentFragment?.arguments?.getInt(Constants.Keys.CURRENT_LIST_POSITION.value)
    private fun getInitPosition() = parentFragment?.arguments?.getInt(Constants.Keys.INIT_LIST_POSITION.value)

    private fun getAdapter() : ListViewContract.Adapter{
        val config = PageConfig
        dataSource = HolidayDataSource(context!!, getYear())
        val list = PagedList(dataSource, config)
        val diffCallback = HolidayDiffUtilCallback(dataSource.getOldData(), dataSource.getNewData())
        val adapter = HolidaysAdapter(context!!, diffCallback)
        adapter.submitList(list.get())
        return adapter
    }

    private fun getYear(): Int{
        val bundle = this.arguments
        return bundle?.getInt(Constants.Keys.YEAR.value, Time().year) ?: Time().year
    }
}