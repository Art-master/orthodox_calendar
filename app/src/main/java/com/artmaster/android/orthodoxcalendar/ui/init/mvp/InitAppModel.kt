package com.artmaster.android.orthodoxcalendar.ui.init.mvp

import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.impl.AppFileParser

class InitAppModel(private val database: AppDatabase,
                   private val fileParser: AppFileParser) : InitAppContract.Model {

    private val connector = App.appComponent.getRepository()

    override fun getDataFromFile(): List<Holiday> {
        return fileParser.getData()
    }

    override fun fillDatabase(data: List<Holiday>) {
        connector.insertHolidays(data)
    }
}