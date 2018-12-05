package com.artmaster.android.orthodoxcalendar.ui.init.mvp

import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.impl.AppFileParser
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity

class InitAppModel(private val database: AppDatabase,
                   private val fileParser: AppFileParser) : InitAppContract.Model {

    override fun getDataFromFile(): List<HolidayEntity> {
        return fileParser.getData()
    }

    override fun fillDatabase(data: List<HolidayEntity>) {
        database.holidaysDb().deleteTable()
        database.holidaysDb().insertAllHolidays(data)
    }
}