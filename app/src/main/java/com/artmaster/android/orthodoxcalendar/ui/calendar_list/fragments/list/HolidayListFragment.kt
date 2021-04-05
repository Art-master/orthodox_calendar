package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.databinding.CalendarListFragmentBinding
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class HolidayListFragment : MvpAppCompatFragment(), ListViewContract {

    private lateinit var recyclerAdapter: ListViewDiffContract.Adapter

    private lateinit var dataSource: HolidayDataSource

    @InjectPresenter(tag = "HolidayListPresenter")
    lateinit var presenter: HolidayListPresenter

    private var _binding: CalendarListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!presenter.isInRestoreState(this)) {
            presenter.attachView(this)

            val e1 = getYear()
            val e2 = getMonth()
            val e3 = getDay()
            lifecycleScope.launchWhenCreated {
                val time = Time(getYear(), getMonth(), getDay())
                presenter.viewIsReady(time)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = CalendarListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
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

    override fun prepareAdapter(position: Int, holiday: Holiday) {
        if (_binding == null) return
        recyclerAdapter = getAdapter(position)
        binding.recyclerView.adapter = recyclerAdapter as RecyclerView.Adapter<*>
    }

    private fun getAdapter(position: Int): ListViewDiffContract.Adapter {
        val config = PageConfig
        dataSource = HolidayDataSource(requireContext(), getYear())
        val list = PagedList(dataSource, config, position)
        val diffCallback = HolidayDiffUtilCallback(dataSource.getOldData(), dataSource.getNewData())
        val adapter = HolidaysAdapter(requireContext(), diffCallback)
        adapter.submitList(list.get())
        return adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}