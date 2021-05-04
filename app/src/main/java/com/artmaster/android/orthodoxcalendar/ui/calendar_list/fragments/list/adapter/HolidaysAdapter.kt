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
import com.artmaster.android.orthodoxcalendar.data.components.TextViewWithCustomFont
import com.artmaster.android.orthodoxcalendar.databinding.ListItemHolidayBinding
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import com.artmaster.android.orthodoxcalendar.ui.review.HolidayViewPagerActivity


class HolidaysAdapter(val context: Context, itemCallback: ListViewDiffContract.CallBack<Holiday>,
                      val filters: ArrayList<Filter> = ArrayList())
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
            val intent = HolidayViewPagerActivity.getIntent(context, item, filters)
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
                holiday.typeId == Holiday.Type.MAIN.id -> {
                    textView.text = context.resources.getString(R.string.main_holiday)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                }
                holiday.typeId == Holiday.Type.TWELVE_MOVABLE.id ||
                        holiday.typeId == Holiday.Type.TWELVE_NOT_MOVABLE.id ||
                        holiday.typeId == Holiday.Type.GREAT_NOT_TWELVE.id -> {
                    textView.text = context.resources.getString(R.string.head_holiday)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }

                holiday.typeId == Holiday.Type.AVERAGE_POLYLEIC.id -> {
                    textView.text = context.resources.getString(R.string.average_polyleic_holiday)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }

                holiday.typeId == Holiday.Type.AVERAGE_PEPPY.id -> {
                    textView.text = context.resources.getString(R.string.average_peppy_holiday)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }

                holiday.typeId == Holiday.Type.COMMON_MEMORY_DAY.id ||
                        holiday.typeId == Holiday.Type.USERS_MEMORY_DAY.id -> {
                    textView.text = context.resources.getString(R.string.memorial_day)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
                }

                else -> {
                    textView.text = ""
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
                }
            }
        }
    }

}