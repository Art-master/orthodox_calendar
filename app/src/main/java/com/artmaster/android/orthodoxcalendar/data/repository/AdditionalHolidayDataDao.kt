package com.artmaster.android.orthodoxcalendar.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.artmaster.android.orthodoxcalendar.domain.AdditionalHolidayData

@Dao
interface AdditionalHolidayDataDao {
    @Insert
    fun insert(data: AdditionalHolidayData)

    @Insert
    fun insertAll(data: List<AdditionalHolidayData>)

    @Query("SELECT * FROM full_holidays_data WHERE holiday_id = :holidayId")
    fun getFullDataByHolidayId(holidayId: Long): AdditionalHolidayData

    @Update
    fun update(data: AdditionalHolidayData)

    @Query("DELETE FROM full_holidays_data WHERE holiday_id = :holidayId")
    fun delete(holidayId: Long)

    @Query("DELETE FROM full_holidays_data WHERE holiday_id = (SELECT id FROM holidays WHERE holidays.id = holiday_id AND created_by_user = 0)")
    fun deleteCommonHolidays()
}
