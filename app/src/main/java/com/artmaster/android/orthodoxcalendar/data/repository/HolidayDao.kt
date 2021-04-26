package com.artmaster.android.orthodoxcalendar.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.artmaster.android.orthodoxcalendar.domain.Holiday

@Dao
interface HolidayDao {
    @Insert
    fun insertHoliday(holiday: Holiday): Long

    @Insert
    fun insertAllHolidays(holidays: List<Holiday>)

    @Query("SELECT * FROM holidays")
    fun getAll(): List<Holiday>

    @Query("SELECT * FROM holidays WHERE type_id IN (:filters)")
    fun getByFilters(filters: List<Int>): List<Holiday>

    /** [monthNum] with 0 */
    @Query("SELECT * FROM holidays WHERE month = :monthNum + 1 OR month = 0")
    fun getByMonth(monthNum: Int): List<Holiday>

    @Query("SELECT * FROM holidays WHERE (month = :month + 1 OR month = 0) AND day = :day")
    fun getHolidaysByDayAndMonth(month: Int, day: Int): List<Holiday>

    @Query("SELECT * FROM holidays WHERE id = :id")
    fun getHolidayById(id: Long): Holiday

    @Query("SELECT * FROM holidays WHERE id >= :initialId LIMIT :loadSize")
    fun getSequence(initialId: Int, loadSize: Int): List<Holiday>

    @Update
    fun update(holiday: Holiday)

    @Query("DELETE FROM holidays WHERE id = :id")
    fun delete(id: Long)

    @Query("DELETE FROM holidays")
    fun deleteTable()
}
