package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter.*
import kotlinx.android.synthetic.main.calendar_list_fragment.*


class HolidayListFragment : MvpAppCompatFragment(), ListViewContract {

    private lateinit var recyclerAdapter: ListViewDiffContract.Adapter

    private lateinit var dataSource : HolidayDataSource

    @InjectPresenter(tag = "HolidayListPresenter", type = PresenterType.LOCAL)
    lateinit var presenter: HolidayListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!presenter.isInRestoreState(this)) {
            presenter.attachView(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        if (!presenter.isInRestoreState(this)) {
            val time = Time(getYear(), getMonth(), getDay())
            presenter.viewIsReady(time)
        }
    }

    private fun getDay(): Int {
        return parentFragment?.arguments?.getInt(Constants.Keys.DAY.value) ?: Time().dayOfMonth
    }

    private fun getMonth(): Int {
        return parentFragment?.arguments?.getInt(Constants.Keys.MONTH.value) ?: Time().monthWith0
    }

    private fun getYear(): Int {
        val bundle = this.arguments
        return bundle?.getInt(Constants.Keys.YEAR.value, Time().year) ?: Time().year
    }

    override fun prepareAdapter(position: Int, holiday: HolidayEntity) {
        recyclerAdapter = getAdapter(position)
        recyclerView?.adapter = recyclerAdapter as RecyclerView.Adapter<*>
    }

    private fun getAdapter(position: Int): ListViewDiffContract.Adapter {
        val config = PageConfig
        dataSource = HolidayDataSource(context!!, getYear())
        val list = PagedList(dataSource, config, position)
        val diffCallback = HolidayDiffUtilCallback(dataSource.getOldData(), dataSource.getNewData())
        val adapter = HolidaysAdapter(context!!, diffCallback)
        adapter.submitList(list.get())
        return adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}