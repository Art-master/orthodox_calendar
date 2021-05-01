package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.databinding.CalendarListFragmentBinding
import com.artmaster.android.orthodoxcalendar.domain.Filter
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

            lifecycleScope.launchWhenCreated {
                val time = Time(getYear(), getMonth(), getDay())
                val filters = requireArguments().getParcelableArrayList<Filter>(Constants.Keys.FILTERS.value)
                presenter.viewIsReady(time, filters ?: emptyList())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CalendarListFragmentBinding.inflate(inflater, container, false)
        binding.loading.root.visibility = View.VISIBLE
        initAnimation()
        return binding.root
    }

    private fun initAnimation() {
        val set = AnimatorInflater.loadAnimator(context, R.animator.loading_animator) as AnimatorSet
        set.setTarget(binding.loading.ringLoading)
        set.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        presenter.viewIsCreated()
    }

    private fun getDay(): Int {
        return arguments?.getInt(Constants.Keys.DAY.value) ?: Time().dayOfMonth
    }

    private fun getMonth(): Int {
        return arguments?.getInt(Constants.Keys.MONTH.value) ?: Time().monthWith0
    }

    private fun getYear(): Int {
        return arguments?.getInt(Constants.Keys.YEAR.value, Time().year) ?: Time().year
    }

    override fun prepareAdapter() {
        val filters = requireArguments().getParcelableArrayList<Filter>(Constants.Keys.FILTERS.value)
        recyclerAdapter = buildAdapter(filters ?: ArrayList(), requireContext())
    }

    override fun showList(position: Int) {
        if (_binding == null) return
        stopLoadingAnimation()
        binding.recyclerView.adapter = recyclerAdapter as RecyclerView.Adapter<*>
        binding.recyclerView.scrollToPosition(position)
        binding.recyclerView.invalidate()
        binding.loading.root.visibility = View.GONE
    }

    private fun stopLoadingAnimation() {
        binding.recyclerView.adapter = recyclerAdapter as RecyclerView.Adapter<*>
        binding.loading.root.visibility = View.GONE
    }

    private fun buildAdapter(filters: ArrayList<Filter>, context: Context): ListViewDiffContract.Adapter {
        val config = PageConfig
        dataSource = HolidayDataSource(context, getYear())
        dataSource.filters = filters
        val list = PagedList(dataSource, config, 0)
        val diffCallback = HolidayDiffUtilCallback(dataSource.getOldData(), dataSource.getNewData())
        val adapter = HolidaysAdapter(context, diffCallback, filters)
        adapter.submitList(list.get())
        return adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}