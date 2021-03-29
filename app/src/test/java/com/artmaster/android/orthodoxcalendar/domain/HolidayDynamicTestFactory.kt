package com.artmaster.android.orthodoxcalendar.domain

import org.junit.Test

abstract class HolidayDynamicTestFactory {

    @Test
    fun checkDynamicDate() {
/*        checkHoliday(2010, DynamicData.THE_EASTER, 4, 4)
        checkHoliday(2018, DynamicData.THE_EASTER, 8, 4)
        checkHoliday(2030, DynamicData.THE_EASTER, 28, 4)
        checkHoliday(2043, DynamicData.THE_EASTER, 3, 5)
        checkHoliday(2049, DynamicData.THE_EASTER, 25, 4)

        checkHoliday(2009,
                DynamicData.THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM, 12, 4)
        checkHoliday(2015,
                DynamicData.THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM, 5, 4)
        checkHoliday(2019,
                DynamicData.THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM, 21, 4)

        checkHoliday(2007,
                DynamicData.THE_ASCENSION_OF_THE_LORD, 17, 5)
        checkHoliday(2018,
                DynamicData.THE_ASCENSION_OF_THE_LORD, 17, 5)
        checkHoliday(2019,
                DynamicData.THE_ASCENSION_OF_THE_LORD, 6, 6)

        checkHoliday(2016, DynamicData.THE_HOLY_TRINITY, 19, 6)
        checkHoliday(2018, DynamicData.THE_HOLY_TRINITY, 27, 5)
        checkHoliday(2021, DynamicData.THE_HOLY_TRINITY, 20, 6)*/
    }

    abstract fun checkHoliday(holiday: Holiday, expectedDay: Int, expectedMonth: Int)
}
