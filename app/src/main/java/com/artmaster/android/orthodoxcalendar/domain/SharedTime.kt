package com.artmaster.android.orthodoxcalendar.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SharedTime(var year: Int = 0, var month: Int = 1, var day: Int = 1) : Parcelable {

    companion object {
        fun isTimeChanged(old: SharedTime, new: SharedTime): Boolean {
            return new.year != old.year || new.month != old.month || new.day != old.day
        }
    }
}