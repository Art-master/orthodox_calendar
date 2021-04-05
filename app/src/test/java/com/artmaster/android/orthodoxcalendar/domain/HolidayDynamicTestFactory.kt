package com.artmaster.android.orthodoxcalendar.domain

import com.artmaster.android.orthodoxcalendar.domain.Holiday.MovableDay.*
import org.junit.Test

abstract class HolidayDynamicTestFactory {

    @Test
    fun checkDynamicDate() {
        checkDynamicHoliday(2010, THE_EASTER, Holiday(month = 4, day = 4))
        checkDynamicHoliday(2018, THE_EASTER, Holiday(month = 4, day = 8))
        checkDynamicHoliday(2030, THE_EASTER, Holiday(month = 4, day = 28))
        checkDynamicHoliday(2043, THE_EASTER, Holiday(month = 5, day = 3))
        checkDynamicHoliday(2049, THE_EASTER, Holiday(month = 4, day = 25))


        checkDynamicHoliday(2009, THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM, Holiday(month = 4, day = 12))
        checkDynamicHoliday(2015, THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM, Holiday(month = 4, day = 5))
        checkDynamicHoliday(2019, THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM, Holiday(month = 4, day = 21))


        checkDynamicHoliday(2007, THE_ASCENSION_OF_THE_LORD, Holiday(month = 5, day = 17))
        checkDynamicHoliday(2018, THE_ASCENSION_OF_THE_LORD, Holiday(month = 5, day = 17))
        checkDynamicHoliday(2019, THE_ASCENSION_OF_THE_LORD, Holiday(month = 6, day = 6))


        checkDynamicHoliday(2016, THE_HOLY_TRINITY, Holiday(month = 6, day = 19))
        checkDynamicHoliday(2018, THE_HOLY_TRINITY, Holiday(month = 5, day = 27))
        checkDynamicHoliday(2021, THE_HOLY_TRINITY, Holiday(month = 6, day = 20))
    }

    abstract fun checkDynamicHoliday(year: Int, type: Holiday.MovableDay, expectedHoliday: Holiday)
}
