package com.artmaster.android.orthodoxcalendar.data.repository

import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.DynamicData
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.impl.AppDataProvider
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.ui.calendar.mvp.CalendarListContract
import java.util.*
import kotlin.collections.ArrayList

/**
 * Get data from storage and prepare it
 */
class DataProvider(private val dataBase: AppDatabase)
    : CalendarListContract.Model, AppDataProvider {

    @Volatile private var dynamicData = DynamicData()

    @Synchronized override fun getMonthDays(month: Int, year: Int): List<Day> {
        dynamicData = DynamicData(year)
        val time = Time()
        time.calendar.set(year, month, 1) // in calendar month with 0
        val daysCount = time.daysInMonth
        val holidaysFromDb = dataBase.holidaysDb().getByMonth(month)
        val days: ArrayList<Day> = ArrayList(daysCount)

        for(i in 1..daysCount){
            time.calendar.set(Calendar.DAY_OF_MONTH, i)
            days.add(createDay(time))
        }

        distributeHoliday(holidaysFromDb, days, month)

        return days
    }

    private fun createDay(time: Time): Day{
        val dayObj = Day(time.year, time.month - 1, time.dayOfMonth, time.dayOfWeek)
        dynamicData.fillFastingDay(dayObj)
        return dayObj
    }

    private fun distributeHoliday(holidays: List<HolidayEntity>, days: ArrayList<Day>, month: Int){
        for(holiday in holidays){
            dynamicData.fillHoliday(holiday)
            val dayNum = holiday.day

            // in holiday entity month with 1
            if(dayNum > days.size || holiday.month != month + 1) continue
            days[dayNum - 1].holidays.add(holiday)
        }
    }

    override fun getData(year: Int): List<HolidayEntity> {
        return getAllData(year).sorted()
    }

    @Synchronized override fun getDataSequence(start: Int, size: Int, year: Int): List<HolidayEntity> {
        return setFirstPosition(getAllData(year).sorted())
    }

    private fun getAllData(year: Int): List<HolidayEntity> {
        val holidaysFromDb: List<HolidayEntity> = getDataFromDb()
        return calculateDynamicData(holidaysFromDb, year)
    }

    private fun calculateDynamicData(holidays:  List<HolidayEntity>, year: Int, month: Int = -1) : List<HolidayEntity>{
        val hds: ArrayList<HolidayEntity> = ArrayList()
        for (holiday in holidays) {
            holiday.year = year
            if (holiday.day == 0) {
                dynamicData.fillHoliday(holiday)
            }
            if(month < 0 || holiday.month == month + 1) hds.add(holiday) //month in holiday with 1
        }
        return hds
    }

    @Synchronized override fun getMonthData(month: Int, year: Int): List<HolidayEntity> {
        val holidaysFromDb = dataBase.holidaysDb().getByMonth(month)
        val holidays = calculateDynamicData(holidaysFromDb, year, month)
        return holidays.sorted()
    }

    @Synchronized override fun getDayData(day: Int, month: Int, year: Int): List<HolidayEntity> {
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