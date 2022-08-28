package com.artmaster.android.orthodoxcalendar.domain

import androidx.compose.runtime.Immutable

@Immutable
data class Day(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val dayInWeek: Int,
    val holidays: ArrayList<Holiday> = ArrayList(),
    val fasting: Fasting = Fasting()
)