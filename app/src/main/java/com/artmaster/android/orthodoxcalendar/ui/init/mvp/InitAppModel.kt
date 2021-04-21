package com.artmaster.android.orthodoxcalendar.ui.init.mvp

import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.domain.FullHolidayData
import com.artmaster.android.orthodoxcalendar.domain.FullHolidayData.Companion.fill
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.impl.AppFileParser

class InitAppModel(private val database: AppDatabase,
                   private val fileParser: AppFileParser) : InitAppContract.Model {

    val context = App.appComponent.getContext()

    override fun getDataFromFile(): List<Holiday> {
        return fileParser.getData()
    }

    override fun fillDatabase(data: List<Holiday>) {
        val holidayDao = database.get(context).holidayDao()
        holidayDao.deleteTable()
        holidayDao.insertAllHolidays(data)

        val fullDataList = data.map { FullHolidayData().fill(it) }
        val fullHolidayDao = database.get(context).fullHolidayDao()
        fullHolidayDao.insertAll(fullDataList)

        database.close()
    }
}