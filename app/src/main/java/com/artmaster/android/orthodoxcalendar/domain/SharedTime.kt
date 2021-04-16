package com.artmaster.android.orthodoxcalendar.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SharedTime(var year: Int = 0, var month: Int = 1, var day: Int = 1) : Parcelable