package com.artmaster.android.orthodoxcalendar.domain

import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity.*
import java.util.Calendar

/**
 * Calculated dynamic holidays
 */
class DynamicData(private val yearEaster: Int = Time().year) {
    companion object {
        const val THE_EASTER = "Пасха"
        const val THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM = "Вход Господень в Иерусалим"
        const val THE_ASCENSION_OF_THE_LORD = "Вознесение Господне"
        const val THE_HOLY_TRINITY = "День Святой Троицы"
    }

    var monthEaster = 0
    var dayEaster = 0

    /**
     * Calculates the date of Easter according to the method of Friedrich Gauss
     *
     * @param year Easter year
     * @return month and day in one variable
     */
    private fun calculateDateEaster(year: Int): Pair<Int, Int> {
        val mod1 = year % 19
        val mod2 = year % 4
        val mod3 = year % 7
        val value1 = mod1 * 19 + 15
        val mod4 = value1 % 30
        val value2 = mod2 * 2
        val value3 = mod3 * 4
        val value4 = mod4 * 6
        val sum1 = value2 + value3 + value4 + 6
        val mod5 = sum1 % 7
        val sum2 = mod4 + mod5

        var day = 0
        var month = 0
        if (sum2 < 9) {
            day = sum2 + 22 + 13
            month = 3
            if (day > 31) {
                day -= 31
                month = 4
            }
        } else if (sum2 > 9) {
            day = sum2 - 9 + 13
            month = 4
            if (day > 30) {
                day -= 30
                month = 5
            }
        }
        monthEaster = month
        dayEaster = day

        return month to day
    }

    fun fillHoliday(holiday: HolidayEntity) {
        var calendar: Calendar? = null
        when (holiday.title) {
            //The Holy Easter
            THE_EASTER -> calendar = getHolidayDynamicDate(yearEaster, 0)
            //The first sunday before Easter
            THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM -> calendar = getHolidayDynamicDate(yearEaster, -7)
            //40 day after Easter
            THE_ASCENSION_OF_THE_LORD -> calendar = getHolidayDynamicDate(yearEaster, 39)
            //50 day after Easter
            THE_HOLY_TRINITY -> calendar = getHolidayDynamicDate(yearEaster, 49)
        }

        if (calendar == null) return
        holiday.day = calendar.get(Calendar.DAY_OF_MONTH)
        holiday.month = calendar.get(Calendar.MONTH) + 1 //in Android API month begin with 0
        calendar.clear()
    }

    /**
     * calculate date the dynamic holiday
     * @param yearEaster current year for holidays
     * @param valueForCalculate days to add or subtract from the date of Easter
     * @return calculated value as Calendar object
     */
    private fun getHolidayDynamicDate(yearEaster: Int, valueForCalculate: Int): Calendar {
        if(monthEaster == 0 || dayEaster == 0) calculateDateEaster(yearEaster)
        return Time().calculateDate(yearEaster, monthEaster - 1, //in Android API month begin with 0
                dayEaster, Calendar.DAY_OF_YEAR, valueForCalculate)
    }

    fun fillFastingDay(day: Day){
        if(day.dayInWeek == DayOfWeek.WEDNESDAY.num) {
            day.fasting.type = Fasting.Type.FASTING_DAY
        }
    }

    private fun isFastingDay(day: Day){

    }
}
