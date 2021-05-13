package com.artmaster.android.orthodoxcalendar.domain

import com.artmaster.android.orthodoxcalendar.domain.Holiday.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Calculated dynamic holidays
 */
class DynamicData {

    private val yearsMapCache = HashMap<Int, Pair<Int, Int>>()

    /**
     * Calculates the date of Easter according to the method of Friedrich Gauss
     *
     * @param year Easter year
     * @return month and day as a Pair
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

        return month - 1 to day //month with 0, day with 1
    }

    fun calcHolidayDateIfDynamic(holiday: Holiday) {
        val time = when (holiday.dynamicType) {
            MovableDay.THE_EASTER.dynamicType ->
                getHolidayDynamicDate(holiday.year, MovableDay.THE_EASTER.dayFromEaster)

            MovableDay.THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM.dynamicType ->
                getHolidayDynamicDate(holiday.year, MovableDay.THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM.dayFromEaster)

            MovableDay.THE_ASCENSION_OF_THE_LORD.dynamicType ->
                getHolidayDynamicDate(holiday.year, MovableDay.THE_ASCENSION_OF_THE_LORD.dayFromEaster)

            MovableDay.THE_HOLY_TRINITY.dynamicType ->
                getHolidayDynamicDate(holiday.year, MovableDay.THE_HOLY_TRINITY.dayFromEaster)

            MovableDay.MEATLESS_SATURDAY.dynamicType ->
                getHolidayDynamicDate(holiday.year, MovableDay.MEATLESS_SATURDAY.dayFromEaster)

            MovableDay.SATURDAY_OF_PARENT_2.dynamicType ->
                getHolidayDynamicDate(holiday.year, MovableDay.SATURDAY_OF_PARENT_2.dayFromEaster)

            MovableDay.SATURDAY_OF_PARENT_3.dynamicType ->
                getHolidayDynamicDate(holiday.year, MovableDay.SATURDAY_OF_PARENT_3.dayFromEaster)

            MovableDay.SATURDAY_OF_PARENT_4.dynamicType ->
                getHolidayDynamicDate(holiday.year, MovableDay.SATURDAY_OF_PARENT_4.dayFromEaster)

            MovableDay.SATURDAY_OF_DMITRY.dynamicType ->
                calculateLastDayOfWeekTime(Month.NOVEMBER.num, 8, holiday.year, Calendar.SATURDAY)

            MovableDay.RADUNYTSYA.dynamicType ->
                getHolidayDynamicDate(holiday.year, MovableDay.RADUNYTSYA.dayFromEaster)

            MovableDay.SATURDAY_OF_PARENT_CHRISTMAS.dynamicType ->
                calculateLastDayOfWeekTime(Month.NOVEMBER.num, 28, holiday.year, Calendar.SATURDAY)

            MovableDay.SATURDAY_OF_TRINITY.dynamicType ->
                getHolidayDynamicDate(holiday.year, MovableDay.SATURDAY_OF_TRINITY.dayFromEaster)

            MovableDay.SATURDAY_OF_PARENT_APOSTLE.dynamicType -> {
                val date = getHolidayDynamicDate(holiday.year, MovableDay.THE_HOLY_TRINITY.dayFromEaster)
                getHolidayDynamicDate(date.monthWith0, date.dayOfMonth, 6, holiday.year)
            }

            MovableDay.SATURDAY_OF_PARENT_ASSUMPTION.dynamicType -> {
                calculateLastDayOfWeekTime(Month.AUGUST.num, 14, holiday.year, Calendar.SATURDAY)
            }

            MovableDay.PERSECUTED.dynamicType -> {
                val time = Time(holiday.year, Month.FEBRUARY.num, 7)
                when (time.dayOfWeek) {
                    DayOfWeek.SUNDAY.num -> time
                    in 0..2 -> {
                        calculateLastDayOfWeekTime(Month.FEBRUARY.num, 7, holiday.year, Calendar.SUNDAY)
                    }
                    else -> calculateNextDayOfWeekTime(Month.FEBRUARY.num, 7, holiday.year, Calendar.SUNDAY)
                }
            }

            else -> null

        } ?: return

        holiday.day = time.dayOfMonth
        holiday.month = time.month
        holiday.monthWith0 = time.monthWith0
    }

    private fun calculateLastDayOfWeekTime(month: Int, day: Int, year: Int, dayOfWeek: Int): Time {
        var index = -1
        var calc = Time().calculateDate(year, month, day, Calendar.DAY_OF_YEAR, index)
        while (calc.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
            calc = Time().calculateDate(year, month, day, Calendar.DAY_OF_YEAR, --index)
        }
        return Time(calc)
    }

    private fun calculateNextDayOfWeekTime(month: Int, day: Int, year: Int, dayOfWeek: Int): Time {
        var index = -1
        var calc = Time().calculateDate(year, month, day, Calendar.DAY_OF_YEAR, index)
        while (calc.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
            calc = Time().calculateDate(year, month, day, Calendar.DAY_OF_YEAR, ++index)
        }
        return Time(calc)
    }

    /**
     * calculate date the dynamic holiday
     * @param yearEaster current year for holidays
     * @param valueForCalculate days to add or subtract from the date of Easter
     * @return calculated value as Calendar object
     */

    private fun getHolidayDynamicDate(yearEaster: Int, valueForCalculate: Int): Time {
        val monthAndDay = getEasterMonthAndDay(yearEaster)

        val cal = Time().calculateDate(yearEaster, monthAndDay.first,
                monthAndDay.second, Calendar.DAY_OF_YEAR, valueForCalculate)
        return Time(cal)
    }

    private fun getEasterMonthAndDay(year: Int): Pair<Int, Int> {
        return if (yearsMapCache.containsKey(year)) {
            yearsMapCache[year] ?: calculateDateEaster(year)
        } else {
            val data = calculateDateEaster(year)
            yearsMapCache[year] = data
            data
        }
    }

    private fun getHolidayDynamicDate(month: Int, day: Int, valueForCalculate: Int, year: Int,
                                      type: Int = Calendar.DAY_OF_YEAR): Time {
        val cal = Time().calculateDate(year, month, day, type, valueForCalculate)
        return Time(cal)
    }

    fun fillFastingDay(day: Day) {
        if (isUsuallyFastingDay(day)) {
            day.fasting.type = Fasting.Type.FASTING_DAY
            day.fasting.permissions = listOf(Fasting.Permission.FISH)
        }
        if (isPeterAndPaulFasting(day)) {
            day.fasting.type = Fasting.Type.FASTING
            fillDayAsPeterFasting(day)
        }
        if (isAssumptionFasting(day)) {
            day.fasting.type = Fasting.Type.FASTING
            fillDayAsAssumptionFasting(day)
        }
        if (isChristmasFasting(day)) {
            day.fasting.type = Fasting.Type.FASTING
            fillDayAsChristmasFasting(day)
        }
        if (isGreatFasting(day)) {
            day.fasting.type = Fasting.Type.FASTING
            fillDayAsGreatFasting(day)
        }
        if (isYole(day) || isBeheadingOfStJohnTheBaptist(day) || isFeastOfTheCross(day)) {
            day.fasting.type = Fasting.Type.FASTING_DAY
            day.fasting.permissions = listOf(Fasting.Permission.STRICT)
        }
    }

    private fun isUsuallyFastingDay(day: Day): Boolean {
        return day.dayInWeek == DayOfWeek.WEDNESDAY.num || day.dayInWeek == DayOfWeek.FRIDAY.num
    }

    private fun isPeterAndPaulFasting(day: Day): Boolean {
        val calendar = getHolidayDynamicDate(day.year, 57)
        val month = calendar.monthWith0
        val dayM = calendar.dayOfMonth

        return (day.month == month && day.dayOfMonth >= dayM) or
                (day.month == Month.JULY.num && day.dayOfMonth <= 12)
    }

    private fun fillDayAsPeterFasting(day: Day) {
        val list = ArrayList<Fasting.Permission>()
        when (day.dayInWeek) {
            DayOfWeek.MONDAY.num -> list.add(Fasting.Permission.HOT_NO_OIL)
            DayOfWeek.TUESDAY.num, DayOfWeek.THURSDAY.num, DayOfWeek.SATURDAY.num, DayOfWeek.SUNDAY.num -> {
                list.add(Fasting.Permission.OIL)
                list.add(Fasting.Permission.FISH)
            }
            DayOfWeek.WEDNESDAY.num, DayOfWeek.FRIDAY.num -> list.add(Fasting.Permission.STRICT)
        }
        day.fasting.permissions = list
        if (day.dayOfMonth == 12) {
            day.fasting.permissions = listOf(Fasting.Permission.FISH)
        }
    }

    private fun isAssumptionFasting(day: Day): Boolean {
        return day.month == Month.AUGUST.num && (day.dayOfMonth in 14..27)
    }

    private fun fillDayAsAssumptionFasting(day: Day) {
        val list = ArrayList<Fasting.Permission>()
        when (day.dayOfMonth) {
            14, 16, 21, 23, 26 -> list.add(Fasting.Permission.STRICT)
            15, 20, 22, 27 -> list.add(Fasting.Permission.HOT_NO_OIL)
            17, 18, 24, 25 -> list.add(Fasting.Permission.OIL)
            28 -> list.add(Fasting.Permission.FISH)
        }
        day.fasting.permissions = list
    }

    private fun isChristmasFasting(day: Day): Boolean {
        return (day.month == Month.NOVEMBER.num && day.dayOfMonth >= 28) or
                (day.month == Month.DECEMBER.num) or
                (day.month == Month.JANUARY.num && day.dayOfMonth <= 6)
    }

    private fun fillDayAsChristmasFasting(day: Day) {
        val list = ArrayList<Fasting.Permission>()
        when {
            day.month == Month.NOVEMBER.num -> {
                when (day.dayOfMonth) {
                    28, 30 -> {
                        list.add(Fasting.Permission.FISH)
                        list.add(Fasting.Permission.OIL)
                    }
                    29 -> list.add(Fasting.Permission.STRICT)
                }
            }
            day.month == Month.DECEMBER.num -> {
                when (day.dayOfMonth) {
                    1, 3, 4, 5, 7, 8, 10, 12, 14, 15, 17, 19, 21, 22, 28, 29 -> {
                        list.add(Fasting.Permission.FISH)
                        list.add(Fasting.Permission.OIL)
                    }
                    2, 9, 16, 23, 30 -> {
                        list.add(Fasting.Permission.HOT_NO_OIL)
                    }
                    6, 11, 13, 18, 20, 25, 27 -> {
                        list.add(Fasting.Permission.STRICT)
                    }
                }
            }
            day.month == Month.JANUARY.num -> {
                when (day.dayOfMonth) {
                    1, 3, 6 -> list.add(Fasting.Permission.STRICT)
                    2 -> list.add(Fasting.Permission.HOT_NO_OIL)
                    4, 5 -> list.add(Fasting.Permission.OIL)
                }
            }
        }
        day.fasting.permissions = list
    }

    private fun isGreatFasting(day: Day): Boolean {
        val calendar = getHolidayDynamicDate(day.year, -48)
        val month = calendar.monthWith0
        val dayM = calendar.dayOfMonth
        val monthToDay = getEasterMonthAndDay(day.year)
        if (day.month > monthToDay.first) return false
        if (day.month < month) return false
        if (day.month == monthToDay.first && day.dayOfMonth < monthToDay.second) return true
        if (day.month == month && day.dayOfMonth >= dayM) return true
        if (day.month in (month + 1) until monthToDay.first) return true
        return false
    }

    private fun isYole(day: Day): Boolean {
        return day.month == Month.JANUARY.num && day.dayOfMonth == 18
    }

    private fun isBeheadingOfStJohnTheBaptist(day: Day): Boolean {
        return day.month == Month.SEPTEMBER.num && day.dayOfMonth == 11
    }

    private fun isFeastOfTheCross(day: Day): Boolean {
        return day.month == Month.SEPTEMBER.num && day.dayOfMonth == 27
    }

    private fun fillDayAsGreatFasting(day: Day) {
        val calendar = getHolidayDynamicDate(day.year, -48)
        val startDay = calendar.monthWith0
        val startMonth = calendar.dayOfMonth

        val list = ArrayList<Fasting.Permission>()
        when (day.dayInWeek) {
            DayOfWeek.MONDAY.num, DayOfWeek.WEDNESDAY.num, DayOfWeek.FRIDAY.num -> {
                list.add(Fasting.Permission.STRICT)
            }
            DayOfWeek.TUESDAY.num, DayOfWeek.THURSDAY.num -> {
                list.add(Fasting.Permission.HOT_NO_OIL)
            }

            DayOfWeek.TUESDAY.num, DayOfWeek.THURSDAY.num -> {
                list.add(Fasting.Permission.OIL)
                list.add(Fasting.Permission.FISH)
            }
            DayOfWeek.SATURDAY.num, DayOfWeek.SUNDAY.num ->
                list.add(Fasting.Permission.OIL)
        }
        day.fasting.permissions = list
        if (day.dayOfMonth == startDay && day.month == startMonth) {
            day.fasting.permissions = listOf(Fasting.Permission.NO_EAT)

        } else {
            val c = getHolidayDynamicDate(day.year, 2)
            val month = c.monthWith0
            val dayM = c.dayOfMonth
            if (dayM == day.dayOfMonth && month == day.month) {
                day.fasting.permissions = listOf(Fasting.Permission.NO_EAT)
            }
        }
        if (day.dayOfMonth == 7 && day.month == Month.APRIL.num) {
            day.fasting.permissions = listOf(Fasting.Permission.FISH)
        }
        if (isHolidayEntry(day)) {
            day.fasting.permissions = listOf(Fasting.Permission.FISH)
        }
        if (isLazarSaturday(day)) {
            day.fasting.permissions = listOf(Fasting.Permission.CAVIAR, Fasting.Permission.OIL)
        }
        if (isLastDayBeforeEaster(day)) {
            day.fasting.permissions = listOf(Fasting.Permission.STRICT)
        }
    }

    private fun isHolidayEntry(day: Day): Boolean {
        getHolidayDynamicDate(day.year, -7).apply {
            return day.dayOfMonth == dayOfMonth && day.month == monthWith0
        }
    }

    private fun isLazarSaturday(day: Day): Boolean {
        getHolidayDynamicDate(day.year, -8).apply {
            return day.dayOfMonth == dayOfMonth && day.month == monthWith0
        }
    }

    private fun isLastDayBeforeEaster(day: Day): Boolean {
        getHolidayDynamicDate(day.year, -6).apply {
            return day.dayOfMonth >= dayOfMonth && (day.month == monthWith0 ||
                    day.month == getEasterMonthAndDay(day.year).first)
        }
    }

    fun fillOtherData(day: Day) {
        setSolidWeek(day)
    }

    fun fillDayInfoByHoliday(day: Day, holiday: Holiday) {
        setMemorialType(day, holiday)
    }

    private fun setMemorialType(day: Day, holiday: Holiday) {
        day.isMemorial = when (holiday.dynamicType) {
            MovableDay.MEATLESS_SATURDAY.dynamicType,
            MovableDay.SATURDAY_OF_PARENT_2.dynamicType,
            MovableDay.SATURDAY_OF_PARENT_3.dynamicType,
            MovableDay.SATURDAY_OF_PARENT_4.dynamicType,
            MovableDay.SATURDAY_OF_TRINITY.dynamicType,
            MovableDay.SATURDAY_OF_DMITRY.dynamicType,
            MovableDay.SATURDAY_OF_PARENT_CHRISTMAS.dynamicType,
            MovableDay.SATURDAY_OF_PARENT_ASSUMPTION.dynamicType,
            MovableDay.SATURDAY_OF_PARENT_APOSTLE.dynamicType,
            MovableDay.PERSECUTED.dynamicType,
            MovableDay.RADUNYTSYA.dynamicType -> true
            else -> false
        }
    }

    private fun setSolidWeek(day: Day) {
        if (day.month == Month.JANUARY.num && day.dayOfMonth in 8..17) {
            day.fasting.type = Fasting.Type.SOLID_WEEK
            day.fasting.permissions = emptyList()
            return
        }
        if (calculateSolidWeek(day, -69, -63)) return
        if (calculateSolidWeek(day, -55, -49, listOf(Fasting.Permission.NO_MEAT))) return
        if (calculateSolidWeek(day, 1, 7)) return
        if (calculateSolidWeek(day, 50, 56)) return
    }

    private fun calculateSolidWeek(day: Day, timeFromEasterStart: Int, timeFromEasterEnd: Int,
                                   permissions: List<Fasting.Permission> = emptyList()): Boolean {
        var flag = false
        val timeStart = getHolidayDynamicDate(day.year, timeFromEasterStart)
        getHolidayDynamicDate(day.year, timeFromEasterEnd).apply {
            if (day.month == monthWith0) {
                if (day.dayOfMonth in timeStart.dayOfMonth until dayOfMonth) {
                    flag = true
                    return@apply
                }
            } else
                if ((day.month == timeStart.monthWith0 && day.dayOfMonth >= timeStart.dayOfMonth) ||
                        (day.month == monthWith0 && day.dayOfMonth < dayOfMonth)) {
                    flag = true
                    return@apply
                }
        }
        if (flag) {
            day.fasting.type = Fasting.Type.SOLID_WEEK
            day.fasting.permissions = permissions
        }
        return flag
    }
}
