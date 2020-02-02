package com.artmaster.android.orthodoxcalendar.data.repository

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.DATABASE_FILE_NAME
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase

@Database(entities = [HolidayEntity::class], version = 1)
abstract class HolidayDatabase : RoomDatabase() {
    abstract fun holidaysDb(): HolidayDao

    companion object : AppDatabase {
        var instance: HolidayDatabase? = null

        override fun get(context: Context): HolidayDatabase {
            synchronized(HolidayDatabase::class) {
                instance = Room.databaseBuilder(
                        context.applicationContext,
                        HolidayDatabase::class.java,
                        DATABASE_FILE_NAME)
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