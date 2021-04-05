package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.data.font.TextViewWithCustomFont
import com.artmaster.android.orthodoxcalendar.databinding.ListItemHolidayBinding
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import com.artmaster.android.orthodoxcalendar.ui.review.HolidayViewPagerActivity


class HolidaysAdapter(val context: Context, itemCallback: ListViewDiffContract.CallBack<Holiday>)
    : PagedListAdapter<Holiday, HolidaysAdapter.HolidayViewHolder>(itemCallback as DiffUtil.ItemCallback<Holiday>),
        ListViewDiffContract.Adapter {

    override fun onCreateViewHolder(viewGroup: ViewGroup, index: Int): HolidayViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding: ListItemHolidayBinding = DataBindingUtil.inflate(inflater, R.layout.list_item_holiday, viewGroup, false)
        return HolidayViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: HolidayViewHolder, index: Int) {
        val item = getItem(index)!!
        viewHolder.itemView.setOnClickListener {
            val intent = HolidayViewPagerActivity.getIntent(context, item)
            context.startActivity(intent)
        }
        viewHolder.bind(getItem(index)!!)
    }

    inner class HolidayViewHolder(binding: ListItemHolidayBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private var bind: ListItemHolidayBinding? = binding

        fun bind(holiday: Holiday) {
            bind!!.holiday = holiday
            bind!!.executePendingBindings()
            getTypyconImageByString(bind!!.holidayTipiconFontIcon, holiday)
        }
    }

    companion object {
        fun getTypyconImageByString(textView: TextViewWithCustomFont, holiday: Holiday) {
            val context = textView.context

            when {
                holiday.type.contains(Holiday.Type.GREAT.value, true) -> {
                    textView.text = context.resources.getString(R.string.head_holiday)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }

                holiday.type.contains(Holiday.Type.AVERAGE_POLYLEIC.value, true) -> {
                    textView.text = context.resources.getString(R.string.average_polyleic_holiday)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }

                holiday.type.contains(Holiday.Type.AVERAGE_PEPPY.value, true) -> {
                    textView.text = context.resources.getString(R.string.average_peppy_holiday)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }

                else -> {
                    textView.text = ""
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
                }
            }
        }
    }

}