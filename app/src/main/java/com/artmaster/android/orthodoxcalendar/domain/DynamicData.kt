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
        if(isUsuallyFastingDay(day)) {
            day.fasting.type = Fasting.Type.FASTING_DAY
        }
        if(isPeterAndPaulFasting(day)){
            day.fasting.type = Fasting.Type.FASTING
            fillDayAsPeterFasting(day)
        }
        if(isAssumptionFasting(day)){
            day.fasting.type = Fasting.Type.FASTING
            fillDayAsAssumptionFasting(day)
        }
        if(isChristmasFasting(day)){
            day.fasting.type = Fasting.Type.FASTING
            fillDayAsChristmasFasting(day)
        }
        if(isGreatFasting(day)){
            day.fasting.type = Fasting.Type.FASTING
        }
    }

    private fun isUsuallyFastingDay(day: Day): Boolean {
        return day.dayInWeek == DayOfWeek.WEDNESDAY.num || day.dayInWeek == DayOfWeek.FRIDAY.num
    }

    private fun isPeterAndPaulFasting(day: Day): Boolean {
        val calendar = getHolidayDynamicDate(yearEaster, 49)
        val month = calendar.get(Calendar.MONTH)
        val dayM = calendar.get(Calendar.DAY_OF_MONTH)

        return (day.month == month && day.dayInMonth >= dayM) or
                (day.month == Month.JULY.num && day.dayInMonth <= 12)
    }

    private fun fillDayAsPeterFasting(day: Day){
        val list = ArrayList<Fasting.Permission>()
        when(day.dayInWeek){
            DayOfWeek.MONDAY.num -> list.add(Fasting.Permission.HOT_NO_OIL)
            DayOfWeek.TUESDAY.num, DayOfWeek.THURSDAY.num, DayOfWeek.SATURDAY.num, DayOfWeek.SUNDAY.num-> {
                list.add(Fasting.Permission.OIL)
                list.add(Fasting.Permission.FISH)
            }
            DayOfWeek.WEDNESDAY.num, DayOfWeek.FRIDAY.num -> list.add(Fasting.Permission.STRICT)
        }
        day.fasting.permissions = list
        if(day.dayInMonth == 12){
            day.fasting.permissions = listOf(Fasting.Permission.FISH)
        }
    }

    private fun isAssumptionFasting(day: Day): Boolean {
        return day.month == Month.AUGUST.num && (day.dayInMonth in 14..28)
    }

    private fun fillDayAsAssumptionFasting(day: Day){
        val list = ArrayList<Fasting.Permission>()
        when (day.dayInMonth) {
            14, 16, 21, 23, 26 -> list.add(Fasting.Permission.STRICT)
            15, 20, 22, 27 -> list.add(Fasting.Permission.HOT_NO_OIL)
            17, 18, 24, 25 -> list.add(Fasting.Permission.OIL)
            28 -> list.add(Fasting.Permission.FISH)
        }
        day.fasting.permissions = list
    }

    private fun isChristmasFasting(day: Day): Boolean{
        return (day.month == Month.NOVEMBER.num && day.dayInMonth >= 28) or
                (day.month == Month.DECEMBER.num) or
                (day.month == Month.JANUARY.num && day.dayInMonth <= 6)
    }

    private fun fillDayAsChristmasFasting(day: Day){
        val list = ArrayList<Fasting.Permission>()
        when {
            day.month == Month.NOVEMBER.num -> {
                when (day.dayInMonth) {
                    28, 30 -> {
                        list.add(Fasting.Permission.FISH)
                        list.add(Fasting.Permission.OIL)
                    }
                    29 -> list.add(Fasting.Permission.STRICT)
                }
            }
            day.month == Month.DECEMBER.num -> {
                when (day.dayInMonth) {
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
                when (day.dayInMonth) {
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
        val month = calendar.get(Calendar.MONTH)
        val dayM = calendar.get(Calendar.DAY_OF_MONTH)
        if (day.month > monthEaster - 1) return false
        if (day.month < month) return false
        if (day.month == monthEaster -1 && day.dayInMonth < dayEaster) return true
        if (day.month == month && day.dayInMonth >= dayM) return true
        if (day.month in (month + 1) until monthEaster - 1) return true
        return false
    }

    private fun fillDayAsGreatFasting(day: Day, startDay : Int, startMonth: Int){
        val list = ArrayList<Fasting.Permission>()
        when(day.dayInWeek){
            DayOfWeek.MONDAY.num, DayOfWeek.WEDNESDAY.num, DayOfWeek.FRIDAY.num->{
                list.add(Fasting.Permission.STRICT)
            }
            DayOfWeek.TUESDAY.num, DayOfWeek.THURSDAY.num-> {
                list.add(Fasting.Permission.HOT_NO_OIL)
            }

            DayOfWeek.TUESDAY.num, DayOfWeek.THURSDAY.num-> {
                list.add(Fasting.Permission.OIL)
                list.add(Fasting.Permission.FISH)
            }
            DayOfWeek.SATURDAY.num, DayOfWeek.SUNDAY.num ->
                list.add(Fasting.Permission.OIL)
        }
        day.fasting.permissions = list
        if(day.dayInMonth == startDay && day.month == startMonth){
            day.fasting.permissions = listOf(Fasting.Permission.NO_EAT)

        } else {
            val calendar = getHolidayDynamicDate(yearEaster, 2)
            val month = calendar.get(Calendar.MONTH)
            val dayM = calendar.get(Calendar.DAY_OF_MONTH)
            if(dayM == day.dayInMonth && month == day.month){
                day.fasting.permissions = listOf(Fasting.Permission.NO_EAT)
            }
        }
        if(day.dayInMonth == 7 && day.month == Month.APRIL.num){
            day.fasting.permissions = listOf(Fasting.Permission.FISH)
        }
        if(isHolidayEntry(day)){
            day.fasting.permissions = listOf(Fasting.Permission.FISH)
        }
        if(isLazarSaturday(day)){
            day.fasting.permissions = listOf(Fasting.Permission.CAVIAR, Fasting.Permission.OIL)
        }
        if(isLastDayBeforeEaster(day)){
            day.fasting.permissions = listOf(Fasting.Permission.STRICT)
        }
    }

    private fun isHolidayEntry(day: Day): Boolean {
        val calendar = getHolidayDynamicDate(yearEaster, -7)
        val month = calendar.get(Calendar.MONTH)
        val dayM = calendar.get(Calendar.DAY_OF_MONTH)
        return day.dayInMonth == dayM && day.month == month
    }

    private fun isLazarSaturday(day: Day): Boolean {
        val calendar = getHolidayDynamicDate(yearEaster, -8)
        val month = calendar.get(Calendar.MONTH)
        val dayM = calendar.get(Calendar.DAY_OF_MONTH)
        return day.dayInMonth == dayM && day.month == month
    }

    private fun isLastDayBeforeEaster(day: Day): Boolean {
        val calendar = getHolidayDynamicDate(yearEaster, -6)
        val month = calendar.get(Calendar.MONTH)
        val dayM = calendar.get(Calendar.DAY_OF_MONTH)
        return day.dayInMonth >= dayM && (day.month == month || day.month == monthEaster)
    }
}
