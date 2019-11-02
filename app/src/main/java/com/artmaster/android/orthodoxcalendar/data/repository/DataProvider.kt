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

    override fun getData(year: Int): List<HolidayEntity> {
        return getAllData(year).sorted()
    }

    override fun getDataSequence(start: Int, size: Int, year: Int): List<HolidayEntity> {
        return setFirstPosition(getAllData(year).sorted())
    }

    private fun getAllData(year: Int): List<HolidayEntity> {
        val holidaysFromDb: List<HolidayEntity> = getDataFromDb()
        for (holiday in holidaysFromDb) {
            holiday.year = year
            if (holiday.day == 0) {
                val dynamic = DynamicHoliday(year, holiday.title)
                holiday.day = dynamic.day
                holiday.month = dynamic.month
            }
        }
        return holidaysFromDb
    }
    override fun getMonthData(month: Int, year: Int): List<HolidayEntity> {
        val holidaysFromDb: List<HolidayEntity> = getAllData(year)
        val holidaysInMonth: ArrayList<HolidayEntity> = ArrayList()
        for (holiday in holidaysFromDb) {
            if(holiday.month == month) holidaysInMonth.add(holiday)
        }
        return holidaysInMonth.sorted()
    }

    override fun getDayData(day: Int, month: Int, year: Int): List<HolidayEntity> {
        val holidaysInMonth = getMonthData(month, year)
        val holidaysInDay: ArrayList<HolidayEntity> = ArrayList()
        for (holiday in holidaysInMonth) {
            if(holiday.day == day) holidaysInDay.add(holiday)
        }
        return holidaysInDay
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