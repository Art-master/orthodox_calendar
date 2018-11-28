package com.artmaster.android.orthodoxcalendar.presentation.calendar

import android.support.v7.util.DiffUtil
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.presentation.calendar.impl.ListViewContract


class HolidayDiffUtilCallback(oldList: List<HolidayEntity>, newList: List<HolidayEntity>)
    : DiffUtil.ItemCallback<HolidayEntity>(), ListViewContract.CallBack<HolidayEntity> {

    private var old: List<HolidayEntity>? = oldList
    private var new: List<HolidayEntity>? = newList

    override fun areItemsTheSame(oldItem: HolidayEntity, newItem: HolidayEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HolidayEntity, newItem: HolidayEntity): Boolean {
        return oldItem.uuid == newItem.uuid
    }
}