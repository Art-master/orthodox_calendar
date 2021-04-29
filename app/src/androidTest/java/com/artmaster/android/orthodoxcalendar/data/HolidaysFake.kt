package com.artmaster.android.orthodoxcalendar.data

import com.artmaster.android.orthodoxcalendar.domain.Holiday
import java.util.*

class HolidaysFake {

    fun get(num: Int): List<Holiday> {
        val range = 1..num
        val holidays = ArrayList<Holiday>(range.last)
        for (i in range) {
            val holiday = getOne(i)
            holidays.add(holiday)
        }
        return holidays.toList()
    }

    fun getOne(index: Int = 3): Holiday {
        val holiday = Holiday()
        holiday.title = "holiday - $index"
        holiday.day = index.inc()
        holiday.month = index.dec()
        holiday.description = "description - $index"
        holiday.imageId = "image - $index"
        holiday.year = index * 100
        return holiday
    }

}