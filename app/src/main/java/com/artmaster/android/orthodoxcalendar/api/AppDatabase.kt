package com.artmaster.android.orthodoxcalendar.api

import android.content.Context
import com.artmaster.android.orthodoxcalendar.data.repository.HolidayDatabase

interface AppDatabase {
    fun get(context: Context): HolidayDatabase
    fun close()

    companion object
}