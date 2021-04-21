package com.artmaster.android.orthodoxcalendar.data.repository

import androidx.room.Dao
import androidx.room.Insert
import com.artmaster.android.orthodoxcalendar.domain.FullHolidayData

@Dao
interface FullHolidayDao {
    @Insert
    fun insert(data: FullHolidayData): FullHolidayData

    @Insert
    fun insertAll(data: List<FullHolidayData>): Long
}
