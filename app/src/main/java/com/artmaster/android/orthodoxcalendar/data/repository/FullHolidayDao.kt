package com.artmaster.android.orthodoxcalendar.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.artmaster.android.orthodoxcalendar.domain.FullHolidayData

@Dao
interface FullHolidayDao {
    @Insert
    fun insert(data: FullHolidayData)

    @Insert
    fun insertAll(data: List<FullHolidayData>)

    @Query("SELECT * FROM full_holidays_data WHERE holiday_id = :holidayId")
    fun getFullDataByHolidayId(holidayId: Long): FullHolidayData
}
