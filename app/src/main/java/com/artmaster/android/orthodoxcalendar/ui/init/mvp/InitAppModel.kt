package com.artmaster.android.orthodoxcalendar.ui.init.mvp

import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.impl.AppFileParser

class InitAppModel(private val database: AppDatabase,
                   private val fileParser: AppFileParser) : InitAppContract.Model {

    override fun getDataFromFile(): List<Holiday> {
        return fileParser.getData()
    }

    override fun fillDatabase(data: List<Holiday>) {
        val dbInstance = database.get(App.appComponent.getContext()).holidayDao()
        dbInstance.deleteTable()
        dbInstance.insertAllHolidays(data)
        database.close()
    }
}