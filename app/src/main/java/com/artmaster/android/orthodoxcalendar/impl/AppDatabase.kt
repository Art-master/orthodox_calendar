package com.artmaster.android.orthodoxcalendar.impl

import com.artmaster.android.orthodoxcalendar.data.repository.HolidayDao

interface AppDatabase {
    fun holidaysDb(): HolidayDao
}