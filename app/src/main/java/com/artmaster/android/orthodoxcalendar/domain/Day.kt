package com.artmaster.android.orthodoxcalendar.domain

import androidx.compose.runtime.Stable

@Stable
data class Day(
    val year: Int,
    val month: Int, //with 0
    val dayOfMonth: Int,
    val dayInWeek: Int,
    val holidays: ArrayList<Holiday> = ArrayList(),
    val fasting: Fasting = Fasting()
)