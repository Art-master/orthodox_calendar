package com.artmaster.android.orthodoxcalendar.domain

import android.os.Parcelable
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Settings.Name
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Filter(val settingName: Name, val resId: Int, var enabled: Boolean = false) :
    Parcelable {

    EASTER(Name.FILTER_EASTER_HOLIDAY, R.string.filter_easter),
    HEAD_HOLIDAYS(Name.FILTER_HEAD_HOLIDAY, R.string.filter_head_holidays),
    AVERAGE_HOLIDAYS(Name.FILTER_AVERAGE_HOLIDAYS, R.string.filter_average_holidays),
    COMMON_MEMORY_DAYS(Name.FILTER_COMMON_MEMORY_DAYS, R.string.filter_common_memory_days),
    MEMORY_DAYS(Name.FILTER_MEMORY_DAYS, R.string.filter_memory_days),
    NAME_DAYS(Name.FILTER_NAME_DAYS, R.string.filter_name_days),
    BIRTHDAYS(Name.BIRTHDAYS, R.string.filter_birthdays)
}