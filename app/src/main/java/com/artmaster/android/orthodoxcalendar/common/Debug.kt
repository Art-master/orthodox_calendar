package com.artmaster.android.orthodoxcalendar.common

import androidx.multidex.BuildConfig


object Debug {

    object Notification {
        fun debugEnabled() = isDebugBuild() && false
        fun getNotificationPeriodMs() = System.currentTimeMillis() + 10_000
    }

    object Time {
        fun debugEnabled() = isDebugBuild() && false
        fun getYear() = 2025
        fun getMonth() = 7
        fun getDay() = 14
    }

    fun isDebugBuild() = BuildConfig.DEBUG

}

