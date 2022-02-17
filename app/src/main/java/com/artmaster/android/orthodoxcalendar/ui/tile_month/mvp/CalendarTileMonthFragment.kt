package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.SharedTime
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.tile_month.components.HolidayTileMonthLayout
import moxy.MvpAppCompatFragment

internal class CalendarTileMonthFragment : MvpAppCompatFragment() {

    private val viewModel: CalendarViewModel by viewModels({ requireParentFragment() })

    private var time: SharedTime = SharedTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        time = requireArguments().getParcelable(Constants.Keys.TIME.value) ?: SharedTime()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HolidayTileMonthLayout(
                    days = viewModel.getCurrentMonthData(),
                    time = viewModel.getSelectedTime()
                )
            }
        }
    }

    private fun getFilters(): ArrayList<Filter> {
        return requireArguments().getParcelableArrayList(Constants.Keys.FILTERS.value)!!
    }
}
