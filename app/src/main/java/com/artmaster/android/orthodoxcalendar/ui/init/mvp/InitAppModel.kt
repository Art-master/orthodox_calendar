package com.artmaster.android.orthodoxcalendar.ui.init.mvp

import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.impl.AppFileParser

class InitAppModel(private val database: AppDatabase,
                   private val fileParser: AppFileParser) : InitAppContract.Model {

    override fun getDataFromFile(): List<HolidayEntity> {
        return fileParser.getData()
    }

    override fun fillDatabase(data: List<HolidayEntity>) {
        val dbInstance = database.get(App.appComponent.getContext()).holidaysDb()
        dbInstance.deleteTable()
        dbInstance.insertAllHolidays(data)
        database.close()
    }
}