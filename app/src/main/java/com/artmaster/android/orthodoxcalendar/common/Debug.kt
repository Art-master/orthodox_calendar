package com.artmaster.android.orthodoxcalendar.common

import androidx.multidex.BuildConfig


object Debug {

    object Notification {
        fun debugEnabled() = false
        fun getNotificationPeriodMs() = System.currentTimeMillis() + 10_000
    }

    object Time {
        fun debugEnabled() = false
        fun getYear() = 2025
        fun getMonthWith0() = 10
        fun getDay() = 25
    }

    fun isDebugBuild() = BuildConfig.DEBUG

}

