package com.artmaster.android.orthodoxcalendar.domain

data class Day(
        var year: Int = 0,
        var month: Int = 0,
        var dayOfMonth: Int = 0,
        var dayInWeek: Int = 0,
        var holidays: ArrayList<Holiday> = ArrayList(),
        var fasting: Fasting = Fasting(),
        var isMemorial: Boolean = false
)