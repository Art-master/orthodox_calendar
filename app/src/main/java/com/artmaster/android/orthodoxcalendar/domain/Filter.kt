package com.artmaster.android.orthodoxcalendar.domain

import android.os.Parcelable
import com.artmaster.android.orthodoxcalendar.common.Settings.Name
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Filter(val settingName: Name) : Parcelable {
    EASTER(Name.FILTER_EASTER_HOLIDAY),
    HEAD_HOLIDAYS(Name.FILTER_HEAD_HOLIDAY),
    AVERAGE_HOLIDAYS(Name.FILTER_AVERAGE_HOLIDAYS),
    COMMON_MEMORY_DAYS(Name.FILTER_COMMON_MEMORY_DAYS),
    MEMORY_DAYS(Name.FILTER_MEMORY_DAYS),
    NAME_DAYS(Name.FILTER_NAME_DAYS)
}