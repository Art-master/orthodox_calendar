package com.artmaster.android.orthodoxcalendar.common

import com.artmaster.android.orthodoxcalendar.BuildConfig

object Debug {

    object Notification {
        fun debugEnabled() = isDebugBuild() && false
        fun getNotificationPeriodMs() = System.currentTimeMillis() + 10_000
    }

    object Time {
        fun debugEnabled() = isDebugBuild() && false
        fun getYear() = 2023
        fun getMonth() = 10
        fun getDay() = 25
    }

    fun isDebugBuild() = BuildConfig.DEBUG

}

