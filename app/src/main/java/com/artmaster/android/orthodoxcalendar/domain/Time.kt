package com.artmaster.android.orthodoxcalendar.domain

import java.util.*

/**
 * Wrapper for work with time
 */
object Time {

    val year: Int
        get() = calendar.get(Calendar.YEAR)

    val dayOfMonth: Int
        get() = calendar.get(Calendar.DAY_OF_MONTH)

    val daysInMonth: Int
        get() = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val dayOfWeek: Int
        get() = calendar.get(Calendar.DAY_OF_WEEK)

    val dayOfYear: Int
        get() = calendar.get(Calendar.DAY_OF_YEAR)

    // First month is 1
    val month: Int
        get() = calendar.get(Calendar.MONTH) + 1

    val hour: Int
        get() = calendar.get(Calendar.HOUR_OF_DAY)

    val minutes: Int
        get() = calendar.get(Calendar.MINUTE)

    /**
     * init the time object
     */
    val calendar: Calendar
        get() {
            val date = Date()
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.timeZone = TimeZone.getDefault()
            return calendar
        }

    fun calculateDate(year: Int, month: Int, day: Int, param: Int, dateCalc: Int): Calendar {
        return calculateDate(year, month, day, 0, param, dateCalc)
    }

    /**
     * Calculate date
     * @param month begin with 0
     * @param param parameter calculate (HOUR, DAY_OF_YEAR, and other)
     * @param dateCalc calculated data (num days or hours or other) May be minus in value
     * @return calendar object
     */
    fun calculateDate(year: Int, month: Int, day: Int, hour: Int, param: Int, dateCalc: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getDefault()
        calendar.set(year, month, day, hour, 0)
        calendar.add(param, dateCalc)
        return calendar
    }

    fun setGregorianCalendar(): Calendar {
        val date = Date()
        val calendar = GregorianCalendar()
        calendar.time = date
        calendar.timeZone = TimeZone.getDefault()
        return calendar
    }
/*    fun getDay(){
        calendar.get(Calendar.)
    }*/


}
