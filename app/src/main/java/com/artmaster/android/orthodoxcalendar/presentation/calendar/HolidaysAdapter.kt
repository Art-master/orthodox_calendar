package com.artmaster.android.orthodoxcalendar.presentation.calendar

import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.databinding.ListItemHolidayBinding
import com.artmaster.android.orthodoxcalendar.presentation.calendar.impl.ListViewContract

class HolidaysAdapter(itemCallback: ListViewContract.CallBack<HolidayEntity>)
    : PagedListAdapter<HolidayEntity,
        HolidaysAdapter.HolidayViewHolder>(itemCallback as DiffUtil.ItemCallback<HolidayEntity>),
        ListViewContract.Adapter {

    override fun onCreateViewHolder(viewGroup: ViewGroup, index: Int): HolidayViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding: ListItemHolidayBinding = DataBindingUtil.inflate(inflater, R.layout.list_item_holiday, viewGroup, false)
        return HolidayViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: HolidayViewHolder, index: Int) {
        viewHolder.bind(getItem(index))
    }

    inner class HolidayViewHolder(binding: ListItemHolidayBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private var bind: ListItemHolidayBinding? = binding

        fun bind(holiday: HolidayEntity?) {
            bind!!.holiday = holiday
            bind!!.executePendingBindings()
        }
    }
}