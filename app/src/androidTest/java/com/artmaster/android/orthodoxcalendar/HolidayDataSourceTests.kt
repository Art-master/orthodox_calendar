package com.artmaster.android.orthodoxcalendar

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.artmaster.android.orthodoxcalendar.data.repository.FileParser
import com.artmaster.android.orthodoxcalendar.data.repository.HolidayDao
import com.artmaster.android.orthodoxcalendar.data.repository.HolidayDatabase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HolidayDataSourceTests {
    private lateinit var database: HolidayDatabase

    private lateinit var dao: HolidayDao

    @Before
    fun fillDatabaseByHolidays() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                HolidayDatabase::class.java).build()

        val holidays = FileParser(App.appComponent.getContext()).getData()
        dao = database.holidayDao()
        dao.insertAllHolidays(holidays)
    }

    @Test
    fun selectDataByYears() {

    }
}