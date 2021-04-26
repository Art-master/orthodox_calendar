package com.artmaster.android.orthodoxcalendar.data.repository

import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.domain.*
import com.artmaster.android.orthodoxcalendar.domain.AdditionalHolidayData.Companion.fill
import com.artmaster.android.orthodoxcalendar.domain.Holiday.Companion.mergeFullData
import com.artmaster.android.orthodoxcalendar.impl.RepositoryConnector
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.impl.CalendarListContractModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * Get data from storage and prepare it
 */
class DataProvider : CalendarListContractModel, RepositoryConnector {

    @Volatile
    private var dynamicData = DynamicData()

    @Volatile
    private var database = App.appComponent.getDatabase()

    private val context = App.appComponent.getContext()

    @Synchronized
    override fun getMonthDays(month: Int, year: Int, filters: ArrayList<Filter>): List<Day> {
        val time = Time()
        time.calendar.set(year, month, 1) // in calendar month with 0
        val daysCount = time.daysInMonth

        val db = database.get(context)
        val holidaysFromDb = db.holidayDao().getByMonth(month)
        val days: ArrayList<Day> = ArrayList(daysCount)

        for (i in 1..daysCount) {
            time.calendar.set(Calendar.DAY_OF_MONTH, i)
            days.add(createDay(time))
        }
        val typeIds = getTypeIds(filters)
        distributeHoliday(holidaysFromDb, days, month, year, typeIds)

        db.close()
        return days
    }

    private fun createDay(time: Time): Day {
        val dayObj = Day(time.year, time.monthWith0, time.dayOfMonth, time.dayOfWeek)
        dynamicData.fillFastingDay(dayObj)
        dynamicData.fillOtherData(dayObj)
        return dayObj
    }

    private fun distributeHoliday(holidays: List<Holiday>, days: ArrayList<Day>, month: Int, year: Int,
                                  typeIds: ArrayList<Int>) {

        for (holiday in holidays) {
            holiday.year = year
            dynamicData.fillHoliday(holiday)
            val dayNum = holiday.day
            holiday.monthWith0 = holiday.month - 1

            if (typeIds.isNotEmpty() && typeIds.contains(holiday.typeId).not()) continue

            if (dayNum > days.size || holiday.monthWith0 != month) continue
            days[dayNum - 1].holidays.add(holiday)
        }
    }

    override fun getData(year: Int, filters: List<Filter>): List<Holiday> {
        return getAllData(year, filters).sorted()
    }

    @Synchronized
    override fun getDataSequence(start: Int, size: Int, year: Int, filters: List<Filter>): List<Holiday> {
        val data = setFirstPosition(getAllData(year, filters).sorted())
        var endPosition = start + size - 1
        if (endPosition > data.size - 1) endPosition = data.size
        return data.subList(start, endPosition)
    }

    private fun getAllData(year: Int, filters: List<Filter>): List<Holiday> {
        val holidaysFromDb: List<Holiday> = getDataFromDb()
        val typeIds = getTypeIds(filters)
        return if (typeIds.isEmpty()) {
            calculateDynamicData(holidaysFromDb, year)
        } else calculateDynamicData(holidaysFromDb, year).filter { typeIds.contains(it.typeId) }
    }

    private fun calculateDynamicData(holidays: List<Holiday>, year: Int, month: Int = -1): List<Holiday> {
        val hds: ArrayList<Holiday> = ArrayList()
        for (holiday in holidays) {
            holiday.year = year
            if (holiday.day == 0) {
                dynamicData.fillHoliday(holiday)
            }

            holiday.monthWith0 = holiday.month - 1
            if (month < 0 || holiday.monthWith0 == month) hds.add(holiday) //month in holiday with 1
        }
        return hds
    }

    @Synchronized
    override fun getMonthData(month: Int, year: Int): List<Holiday> {
        val db = database.get(context)
        val holidaysFromDb = db.holidayDao().getByMonth(month)
        db.close()
        val holidays = calculateDynamicData(holidaysFromDb, year, month)
        return holidays.sorted()
    }

    @Synchronized
    override fun getDayData(day: Int, month: Int, year: Int): List<Holiday> {
        val holidaysInMonth = getMonthData(month, year)
        val holidaysInDay: ArrayList<Holiday> = ArrayList()
        for (holiday in holidaysInMonth) {
            if (holiday.day == day) holidaysInDay.add(holiday)
        }
        return holidaysInDay
    }

    private fun getDataFromDb(): List<Holiday> {
        val data = HolidaysCache.holidays
        if (data.isEmpty().not()) return data
        val db = database.get(context)
        val holidays = db.holidayDao().getAll()
        HolidaysCache.holidays = holidays
        db.close()
        return holidays
    }

    private fun getTypeIds(filters: List<Filter>): ArrayList<Int> {
        val typeIds = ArrayList<Int>()
        filters.forEach { typeIds.addAll(Holiday.getTypeIdsByFilter(it)) }
        return typeIds
    }

    private fun setFirstPosition(holidays: List<Holiday>): List<Holiday> {
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

    @Synchronized
    override fun getHolidaysByTime(time: Time): List<Holiday> {
        val db = database.get(context)
        val holidays = db.holidayDao().getHolidaysByDayAndMonth(time.monthWith0, time.dayOfMonth)
        db.close()
        return calculateDynamicData(holidays, time.year)
    }

    override fun insert(holiday: Holiday) {
        val fullHolidayDao = database.get(context).additionalHolidayDataDao()
        val holidayDao = database.get(context).holidayDao()
        val id = holidayDao.insertHoliday(holiday)

        val additionalData = AdditionalHolidayData().fill(holiday)
        additionalData.holidayId = id
        fullHolidayDao.insert(additionalData)
        HolidaysCache.holidays = emptyList()
        database.close()
    }

    override fun insertHolidays(holidays: List<Holiday>) {
        val holidayDao = database.get(context).holidayDao()
        holidayDao.deleteTable()
        holidayDao.insertAllHolidays(holidays)

        val fullDataList = holidays.map { AdditionalHolidayData().fill(it) }
        val fullHolidayDao = database.get(context).additionalHolidayDataDao()
        fullHolidayDao.insertAll(fullDataList)
        database.close()
    }

    override fun update(holiday: Holiday) {
        val holidayDao = database.get(context).holidayDao()
        holidayDao.update(holiday)
        HolidaysCache.holidays = emptyList()
        val additionalData = AdditionalHolidayData().fill(holiday)
        val additionalHolidayDataDao = database.get(context).additionalHolidayDataDao()
        additionalHolidayDataDao.update(additionalData)
        database.close()
    }

    override fun getFullHolidayData(id: Long): Holiday {
        val holidayDao = database.get(context).holidayDao()
        val fullHolidayDao = database.get(context).additionalHolidayDataDao()
        val holiday = holidayDao.getHolidayById(id)
        return holiday.mergeFullData(fullHolidayDao.getFullDataByHolidayId(holiday.id))
    }

    override fun deleteById(id: Long) {
        val holidayDao = database.get(context).holidayDao()
        holidayDao.delete(id)
        val fullHolidayDao = database.get(context).additionalHolidayDataDao()
        HolidaysCache.holidays = emptyList()
        fullHolidayDao.delete(id)
        database.close()
    }
}