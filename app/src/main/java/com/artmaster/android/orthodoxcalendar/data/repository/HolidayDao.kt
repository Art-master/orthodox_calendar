package com.artmaster.android.orthodoxcalendar.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday

@Dao
interface HolidayDao {
    @Insert
    fun insertHoliday(holiday: Holiday): Long

    @Insert
    fun insertAllHolidays(holidays: List<Holiday>)

    @Query("SELECT * FROM holidays")
    fun getAll(): List<Holiday>

    @Query("SELECT * FROM holidays WHERE type")
    fun getByFilters(filters: List<Filter>): List<Holiday>

    /** [monthNum] with 0 */
    @Query("SELECT * FROM holidays WHERE month = :monthNum + 1 OR month = 0")
    fun getByMonth(monthNum: Int): List<Holiday>

    @Query("SELECT * FROM holidays WHERE (month = :month + 1 OR month = 0) AND day = :day")
    fun getHolidaysByDayAndMonth(month: Int, day: Int): List<Holiday>

    @Query("SELECT * FROM holidays WHERE id = :id")
    fun getForId(id: Long): Holiday

    @Query("SELECT * FROM holidays WHERE id >= :initialId LIMIT :loadSize")
    fun getSequence(initialId: Int, loadSize: Int): List<Holiday>

    @Query("DELETE FROM holidays")
    fun deleteTable()
}
