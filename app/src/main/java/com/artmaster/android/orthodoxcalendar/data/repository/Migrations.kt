package com.artmaster.android.orthodoxcalendar.data.repository

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.artmaster.android.orthodoxcalendar.App

class Migrations {
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DELETE FROM holidays")
                val holidays = FileParser(App.appComponent.getContext()).getData()
                val db = database as HolidayDatabase
                db.holidayDao().insertAllHolidays(holidays)
            }
        }
    }
}