package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.data.font.TextViewWithCustomFont
import com.artmaster.android.orthodoxcalendar.databinding.ListItemHolidayBinding
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import com.artmaster.android.orthodoxcalendar.ui.review.HolidayViewPagerActivity
import org.jetbrains.anko.textColor


class HolidaysAdapter(val context: Context, itemCallback: ListViewDiffContract.CallBack<HolidayEntity>)
    : PagedListAdapter<HolidayEntity, HolidaysAdapter.HolidayViewHolder>(itemCallback as DiffUtil.ItemCallback<HolidayEntity>),
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

        fun bind(holiday: HolidayEntity) {
            bind!!.holiday = holiday
            bind!!.executePendingBindings()
            getTypiconImageByString(bind!!.holidayTipiconFontIcon, holiday)
        }
    }

    companion object {
        fun getTypiconImageByString(textView: TextViewWithCustomFont, holiday : HolidayEntity) {
            val context = textView.context
            textView.textColor = ContextCompat.getColor(context, R.color.colorRed)

            when {
                holiday.type.contains(HolidayEntity.Type.GREAT.value, true) ->
                    textView.text = context.resources.getString(R.string.head_holiday)

                holiday.type.contains(HolidayEntity.Type.AVERAGE_POLYLEIC.value, true) ->
                    textView.text = context.resources.getString(R.string.average_polyleic_holiday)

                holiday.type.contains(HolidayEntity.Type.AVERAGE_PEPPY.value, true) ->
                    textView.text = context.resources.getString(R.string.average_peppy_holiday)

                else -> textView.text = ""
            }
        }
    }

}