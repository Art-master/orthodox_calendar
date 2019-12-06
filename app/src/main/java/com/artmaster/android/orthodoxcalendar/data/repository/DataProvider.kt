package com.artmaster.android.orthodoxcalendar.data.repository

import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.DynamicData
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.impl.AppDataProvider
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.ui.calendar.mvp.CalendarListContract

/**
 * Get data from storage and prepare it
 */
class DataProvider(private val dataBase: AppDatabase)
    : CalendarListContract.Model, AppDataProvider {

    var dynamicData = DynamicData()

    override fun getMonthDays(month: Int, year: Int): List<Day> {
        dynamicData = DynamicData(year)
        val time = Time()
        time.calendar.set(year, month, 0)
        val daysCount = time.daysInMonth
        val holidaysFromDb = dataBase.holidaysDb().getByMonth(month)
        val days: ArrayList<Day> = ArrayList(daysCount)

        for(i in 1..daysCount){
            days.add(createDay(year, month, i))
        }

        distributeHoliday(holidaysFromDb, days)

        return days
    }

    private fun createDay(year: Int, month: Int, day: Int): Day{
        val dayObj = Day(month, day)
        dynamicData.fillFastingDay(dayObj)
        return dayObj
    }

    private fun distributeHoliday(holidays: List<HolidayEntity>, days: ArrayList<Day>){
        for(holiday in holidays){
            val dayNum = holiday.day
            if(dayNum > days.size) continue
            days[dayNum].holidays.add(holiday)
        }
    }

    override fun getData(year: Int): List<HolidayEntity> {
        return getAllData(year).sorted()
    }

    override fun getDataSequence(start: Int, size: Int, year: Int): List<HolidayEntity> {
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

    override fun getMonthData(month: Int, year: Int): List<HolidayEntity> {
        val holidaysFromDb = dataBase.holidaysDb().getByMonth(month)
        val holidays = calculateDynamicData(holidaysFromDb, year, month)
        return holidays.sorted()
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