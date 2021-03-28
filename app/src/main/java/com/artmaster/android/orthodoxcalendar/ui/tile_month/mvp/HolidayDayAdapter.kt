package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.databinding.ListItemHolidayBinding
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter.HolidaysAdapter
import com.artmaster.android.orthodoxcalendar.ui.review.HolidayViewPagerActivity

class HolidayDayAdapter(val holidays : List<HolidayEntity>, val context : Context):
        RecyclerView.Adapter<HolidayDayAdapter.HolidayViewHolder>() {

    override fun getItemCount() = holidays.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, index: Int): HolidayViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding: ListItemHolidayBinding = DataBindingUtil.inflate(inflater, R.layout.list_item_holiday, viewGroup, false)
        return HolidayViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: HolidayViewHolder, index: Int) {
        val item = holidays[index]
        viewHolder.itemView.setOnClickListener {
            val intent = HolidayViewPagerActivity.getIntent(context, item)
            context.startActivity(intent)
        }

        viewHolder.bind(holidays[index])
    }

    inner class HolidayViewHolder(binding: ListItemHolidayBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private var bind: ListItemHolidayBinding? = binding

        fun bind(holiday: HolidayEntity) {
            bind?.let {
                bind!!.holiday = holiday
                bind!!.executePendingBindings()
                HolidaysAdapter.getTypiconImageByString(bind!!.holidayTipiconFontIcon, holiday)
            }

        }
    }
}
