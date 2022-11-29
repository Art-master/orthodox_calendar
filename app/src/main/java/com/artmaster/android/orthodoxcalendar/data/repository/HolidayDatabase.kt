package com.artmaster.android.orthodoxcalendar.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.artmaster.android.orthodoxcalendar.api.AppDatabase
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.DATABASE_FILE_NAME
import com.artmaster.android.orthodoxcalendar.domain.AdditionalHolidayData
import com.artmaster.android.orthodoxcalendar.domain.Holiday

@Database(entities = [Holiday::class, AdditionalHolidayData::class], version = 4)
abstract class HolidayDatabase : RoomDatabase() {
    abstract fun holidayDao(): HolidayDao
    abstract fun additionalHolidayDataDao(): AdditionalHolidayDataDao

    companion object : AppDatabase {
        var instance: HolidayDatabase? = null

        override fun get(context: Context): HolidayDatabase {
            synchronized(HolidayDatabase::class) {
                instance = Room.databaseBuilder(
                        context.applicationContext,
                        HolidayDatabase::class.java,
                        DATABASE_FILE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                return instance!!
            }
        }

        override fun close() {
            if (instance?.isOpen == true) {
                instance?.close()
            }

            instance = null
        }
    }
}