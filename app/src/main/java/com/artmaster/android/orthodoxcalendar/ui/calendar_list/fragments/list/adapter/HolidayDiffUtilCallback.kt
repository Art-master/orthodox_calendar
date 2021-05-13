package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract


class HolidayDiffUtilCallback
    : DiffUtil.ItemCallback<Holiday>(), ListViewDiffContract.CallBack<Holiday> {

    override fun areItemsTheSame(oldItem: Holiday, newItem: Holiday): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Holiday, newItem: Holiday): Boolean {
        return oldItem.id == newItem.id
    }
}