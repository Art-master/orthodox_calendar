package com.artmaster.android.orthodoxcalendar.domain

import android.os.Parcelable
import com.artmaster.android.orthodoxcalendar.common.Settings
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Filter(val settingName: Settings.Name) : Parcelable {
    EASTER(Settings.Name.FILTER_EASTER_HOLIDAY),
    HEAD_HOLIDAYS(Settings.Name.FILTER_HEAD_HOLIDAY),
    AVERAGE_HOLIDAYS(Settings.Name.FILTER_AVERAGE_HOLIDAYS),
    COMMON_MEMORY_DAYS(Settings.Name.FILTER_COMMON_MEMORY_DAYS),
    MEMORY_DAYS(Settings.Name.FILTER_MEMORY_DAYS),
    NAME_DAYS(Settings.Name.FILTER_NAME_DAYS)
}