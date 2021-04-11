package com.artmaster.android.orthodoxcalendar.domain

import com.artmaster.android.orthodoxcalendar.domain.Holiday.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Calculated dynamic holidays
 */
class DynamicData(private val yearEaster: Int = Time().year) {

    private var monthEaster = -1
    private var dayEaster = -1

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
        monthEaster = month - 1 //month with 0
        dayEaster = day //day with 1

        return month to day
    }

    fun fillHoliday(holiday: Holiday) {
        val time = when (holiday.dynamicType) {
            MovableDay.THE_EASTER.dynamicType ->
                getHolidayDynamicDate(yearEaster, MovableDay.THE_EASTER.dayFromEaster)

            MovableDay.THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM.dynamicType ->
                getHolidayDynamicDate(yearEaster, MovableDay.THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM.dayFromEaster)

            MovableDay.THE_ASCENSION_OF_THE_LORD.dynamicType ->
                getHolidayDynamicDate(yearEaster, MovableDay.THE_ASCENSION_OF_THE_LORD.dayFromEaster)

            MovableDay.THE_HOLY_TRINITY.dynamicType ->
                getHolidayDynamicDate(yearEaster, MovableDay.THE_HOLY_TRINITY.dayFromEaster)

            else -> null

        } ?: return

        holiday.day = time.dayOfMonth
        holiday.month = time.month
        holiday.monthWith0 = time.monthWith0
    }

    /**
     * calculate date the dynamic holiday
     * @param yearEaster current year for holidays
     * @param valueForCalculate days to add or subtract from the date of Easter
     * @return calculated value as Calendar object
     */

    private fun getHolidayDynamicDate(yearEaster: Int, valueForCalculate: Int): Time {
        if (monthEaster == -1 || dayEaster == -1) calculateDateEaster(yearEaster)
        val cal = Time().calculateDate(yearEaster, monthEaster,
                dayEaster, Calendar.DAY_OF_YEAR, valueForCalculate)
        return Time(cal)
    }

    private fun getHolidayDynamicDate(month: Int, day: Int, valueForCalculate: Int): Time {
        val cal = Time().calculateDate(yearEaster, month,
                day, Calendar.DAY_OF_YEAR, valueForCalculate)
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
        val calendar = getHolidayDynamicDate(yearEaster, 57)
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
        val calendar = getHolidayDynamicDate(yearEaster, -48)
        val month = calendar.monthWith0
        val dayM = calendar.dayOfMonth
        if (day.month > monthEaster) return false
        if (day.month < month) return false
        if (day.month == monthEaster && day.dayOfMonth < dayEaster) return true
        if (day.month == month && day.dayOfMonth >= dayM) return true
        if (day.month in (month + 1) until monthEaster) return true
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
        val calendar = getHolidayDynamicDate(yearEaster, -48)
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
            val c = getHolidayDynamicDate(yearEaster, 2)
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
        getHolidayDynamicDate(yearEaster, -7).apply {
            return day.dayOfMonth == dayOfMonth && day.month == monthWith0
        }
    }

    private fun isLazarSaturday(day: Day): Boolean {
        getHolidayDynamicDate(yearEaster, -8).apply {
            return day.dayOfMonth == dayOfMonth && day.month == monthWith0
        }
    }

    private fun isLastDayBeforeEaster(day: Day): Boolean {
        getHolidayDynamicDate(yearEaster, -6).apply {
            return day.dayOfMonth >= dayOfMonth && (day.month == monthWith0 || day.month == monthEaster)
        }
    }

    fun fillOtherData(day: Day) {
        setMemorialType(day)
        setSolidWeek(day)
    }

    private fun setMemorialType(day: Day) {
        //The saturday of Meatless
        getHolidayDynamicDate(yearEaster, -57).apply {
            if (day.month == monthWith0 && day.dayOfMonth == dayOfMonth) {
                day.memorialType = Day.MemorialType.MEATLESS_SATURDAY
                return@apply
            }
        }

        //The saturday of Great Fasting 2
        getHolidayDynamicDate(yearEaster, -36).apply {
            if (day.month == monthWith0 && day.dayOfMonth == dayOfMonth) {
                day.memorialType = Day.MemorialType.SATURDAY_OF_PARENT_2
                return@apply
            }
        }

        //The saturday of Great Fasting 3
        getHolidayDynamicDate(yearEaster, -29).apply {
            if (day.month == monthWith0 && day.dayOfMonth == dayOfMonth) {
                day.memorialType = Day.MemorialType.SATURDAY_OF_PARENT_3
                return@apply
            }
        }

        //The saturday of Great Fasting 4
        getHolidayDynamicDate(yearEaster, -22).apply {
            if (day.month == monthWith0 && day.dayOfMonth == dayOfMonth) {
                day.memorialType = Day.MemorialType.SATURDAY_OF_PARENT_4
                return@apply
            }
        }

        //Radunytsya
        getHolidayDynamicDate(yearEaster, 9).apply {
            if (day.month == monthWith0 && day.dayOfMonth == dayOfMonth) {
                day.memorialType = Day.MemorialType.RADUNYTSYA
                return@apply
            }
        }

        //Trinity saturday
        getHolidayDynamicDate(yearEaster, 48).apply {
            if (day.month == monthWith0 && day.dayOfMonth == dayOfMonth) {
                day.memorialType = Day.MemorialType.SATURDAY_OF_PARENT_TRINITY
                return@apply
            }
        }

        //The saturday near the Assumption Fasting
        if (day.month == Month.AUGUST.num && day.dayInWeek == DayOfWeek.SATURDAY.num &&
                day.dayOfMonth in 7 until 14) {

            day.memorialType = Day.MemorialType.SATURDAY_OF_PARENT_ASSUMPTION
            return
        }

        //The saturday of st. Dmitry
        getHolidayDynamicDate(Month.NOVEMBER.num, 8, -7).apply {
            if (day.month == Month.NOVEMBER.num && day.dayOfMonth < 8 && day.dayInWeek == DayOfWeek.SATURDAY.num) {
                day.memorialType = Day.MemorialType.SATURDAY_OF_DMITRY
                return@apply
            }
        }

        //The saturday near the Christmas Fasting
        if (day.month == Month.NOVEMBER.num && day.dayInWeek == DayOfWeek.SATURDAY.num &&
                day.dayOfMonth in 21 until 28) {

            day.memorialType = Day.MemorialType.SATURDAY_OF_PARENT_CHRISTMAS
            return
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
        val timeStart = getHolidayDynamicDate(yearEaster, timeFromEasterStart)
        getHolidayDynamicDate(yearEaster, timeFromEasterEnd).apply {
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
