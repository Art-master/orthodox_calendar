package com.artmaster.android.orthodoxcalendar

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.artmaster.android.orthodoxcalendar.data.HolidaysFake
import com.artmaster.android.orthodoxcalendar.data.repository.HolidayDao
import com.artmaster.android.orthodoxcalendar.data.repository.HolidayDatabase
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HolidayDatabaseTests {
    private lateinit var database: AppDatabase

    private lateinit var dao: HolidayDao

    @Before
    fun createDatabase() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                HolidayDatabase::class.java).build()

        dao = (database as HolidayDatabase).holidaysDb()
    }

    @Test
    fun insertAllEntities() {
        val holidays: List<HolidayEntity> = HolidaysFake().get(10)
        dao.insertAllHolidays(holidays)

        val dbHolidays = dao.getAll()

        Assert.assertEquals(holidays.size, dbHolidays.size)
    }

    @Test
    fun insertOneEntity() {
        val holiday = HolidaysFake().getOne()
        val id = dao.insertHoliday(holiday)
        val holidayDb = dao.getForId(id)
        Assert.assertEquals(holidayDb.description, holiday.description)
    }

    @Test
    fun selectSequence() {
        val numEntities = 7
        dao.insertAllHolidays(HolidaysFake().get(numEntities))
        val holidaysDb = dao.getSequence(1, numEntities)
        Assert.assertEquals(holidaysDb.size, numEntities)
    }
}