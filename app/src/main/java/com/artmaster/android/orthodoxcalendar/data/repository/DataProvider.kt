package com.artmaster.android.orthodoxcalendar.data.repository

import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.DynamicHoliday
import com.artmaster.android.orthodoxcalendar.impl.AppDataProvider
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.ui.calendar.mvp.CalendarListContract

/**
 * Get data from storage and prepare it
 */
class DataProvider(private val dataBase: AppDatabase)
    : CalendarListContract.Model, AppDataProvider {

    override fun getData(): List<HolidayEntity> {
        return getAllData().sorted()
    }

    override fun getDataSequence(start: Int, size: Int): List<HolidayEntity> {
        return setFirstPosition(getAllData().sorted())
    }

    private fun getAllData(): List<HolidayEntity> {
        val holidaysFromDb: List<HolidayEntity> = getDataFromDb()
        for (holiday in holidaysFromDb) {
            holiday.year = 2018
            if (holiday.day == 0) {
                val dynamic = DynamicHoliday(2018, holiday.title)
                holiday.day = dynamic.day
                holiday.month = dynamic.month
            }
        }
        return holidaysFromDb
    }

    private fun getDataFromDb(): List<HolidayEntity> {
        return dataBase.holidaysDb().getAll()
    }

    private fun setFirstPosition(holidays: List<HolidayEntity>): List<HolidayEntity> {
        var day = 0
        var month = 0
        for (holiday in holidays) {
            if (month == holiday.month && day == holiday.day) {
                holiday.firstInGroup = false
            } else {
                holiday.firstInGroup = true
                month = holiday.month
                day = holiday.day
            }

        }
        return holidays
    }
}