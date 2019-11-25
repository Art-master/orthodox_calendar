package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.databinding.ListItemHolidayBinding
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
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
        val itemId = holidays[index].uuid
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, HolidayViewPagerActivity::class.java)
            intent.putExtra(Constants.Keys.HOLIDAY_ID.name, itemId)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }
        viewHolder.bind(holidays[index])
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
