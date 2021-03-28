package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract


class HolidayDiffUtilCallback(oldList: List<HolidayEntity>, newList: List<HolidayEntity>)
    : DiffUtil.ItemCallback<HolidayEntity>(), ListViewDiffContract.CallBack<HolidayEntity> {

    private var old: List<HolidayEntity>? = oldList
    private var new: List<HolidayEntity>? = newList

    override fun areItemsTheSame(oldItem: HolidayEntity, newItem: HolidayEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HolidayEntity, newItem: HolidayEntity): Boolean {
        return oldItem.uuid == newItem.uuid
    }
}