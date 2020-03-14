package com.artmaster.android.orthodoxcalendar.domain

import java.util.*

/**
 * Wrapper for work with time
 */
class Time(var calendar: Calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())) {

    constructor(year: Int, month: Int, day: Int) : this() {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
    }

    val year: Int
        get() = calendar.get(Calendar.YEAR)

    val dayOfMonth: Int
        get() = calendar.get(Calendar.DAY_OF_MONTH)

    val daysInMonth: Int
        get() = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val dayOfWeek: Int
        get() {
            val day = calendar.get(Calendar.DAY_OF_WEEK)
            return if(day == 1) 7 else day -1
        }

    val dayOfYear: Int
        get() = calendar.get(Calendar.DAY_OF_YEAR)

    // First month is 1
    val month: Int
        get() = calendar.get(Calendar.MONTH) + 1

    // First month is 1
    val monthWith0: Int
        get() = calendar.get(Calendar.MONTH)

    val hour: Int
        get() = calendar.get(Calendar.HOUR_OF_DAY)

    val minutes: Int
        get() = calendar.get(Calendar.MINUTE)

    fun calculateDate(time: Time, param: Int = Calendar.DAY_OF_YEAR, dateCalc: Int): Calendar {
        return calculateDate(time.year, time.month, time.dayOfMonth, 0, param, dateCalc)
    }

    fun calculateDate(year: Int = this.year, month: Int = this.month, day: Int = this.dayOfMonth,
                      param: Int = Calendar.DAY_OF_YEAR, dateCalc: Int): Calendar {
        return calculateDate(year, month, day, 0, param, dateCalc)
    }

    /**
     * Calculate date
     * @param month begin with 0
     * @param param parameter calculate (HOUR, DAY_OF_YEAR, and other)
     * @param dateCalc calculated data (num days or hours or other) May be minus in value
     * @return calendar object
     */
    fun calculateDate(year: Int = this.year, month: Int = this.month, day: Int = this.dayOfMonth,
                      hour: Int = this.hour, param: Int = Calendar.DAY_OF_YEAR, dateCalc: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getDefault()
        calendar.set(year, month, day, hour, 0)
        calendar.add(param, dateCalc)
        return calendar
    }

    fun getDaysOfYear(year: Int): Int{
        val c = Calendar.getInstance()
        c.set(year, 11, 31)
        return c.get(Calendar.DAY_OF_YEAR)
    }

    fun setGregorianCalendar(): Calendar {
        val date = Date()
        val calendar = GregorianCalendar()
        calendar.time = date
        calendar.timeZone = TimeZone.getDefault()
        return calendar
    }

    enum class Day(val num : Int){
        SUNDAY(7)
    }

    enum class Month(val num : Int){
        DECEMBER(11)
    }
}
