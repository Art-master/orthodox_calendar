package com.artmaster.android.orthodoxcalendar.ui.tile_pager.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_SIZE
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.SharedTime
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.tile_month.components.HolidayTileLayout
import com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp.CalendarTileMonthFragment
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTileView

internal class CalendarTileFragment : Fragment(), ContractTileView {

    private val viewModel: CalendarViewModel by activityViewModels()

    private var filters = ArrayList<Filter>()
    private var time = SharedTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            time = requireArguments().getParcelable(Constants.Keys.TIME.value) ?: SharedTime()
        }

        subscribeToDataUpdate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val currentTime = Time(time.year, time.month, time.day)
                HolidayTileLayout(viewModel, currentTime)
            }
        }
    }

    private fun subscribeToDataUpdate() {
        viewModel.filters.observe(this) { item ->
            filters.clear()
            filters.addAll(item.toList())
        }

        viewModel.time.observe(this) { item ->
            if (SharedTime.isTimeChanged(time, item)) {
                time = item
            }
        }
    }

    private fun getMonthsNames() = resources.getStringArray(R.array.months_names_gen)

    private fun getAdapter(fa: Fragment): FragmentStateAdapter {

        return object : FragmentStateAdapter(fa) {
            override fun getItemCount() = MONTH_SIZE

            override fun createFragment(position: Int): Fragment {
                val fragment = CalendarTileMonthFragment()
                val args = Bundle()
                val data = SharedTime(time.year, position, time.day)
                args.putParcelable(Constants.Keys.TIME.value, data)
                args.putParcelableArrayList(Constants.Keys.FILTERS.value, filters)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
