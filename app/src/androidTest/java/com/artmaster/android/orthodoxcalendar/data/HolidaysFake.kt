package com.artmaster.android.orthodoxcalendar.data

import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import java.util.*

class HolidaysFake {


    fun get(num: Int): List<HolidayEntity> {
        val range = 1..num
        val holidays = ArrayList<HolidayEntity>(range.last)
        for (i in range) {
            val holiday = getOne(i)
            holidays.add(holiday)
        }
        return holidays.toList()
    }

    fun getOne(index: Int = 3): HolidayEntity {
        val holiday = HolidayEntity()
        holiday.uuid = UUID.randomUUID().toString()
        holiday.title = "holiday - $index"
        holiday.day = index.inc()
        holiday.month = index.dec()
        holiday.description = "description - $index"
        holiday.imageLink = "image - $index"
        holiday.type = "type - $index"
        holiday.year = index * 100
        return holiday
    }

}