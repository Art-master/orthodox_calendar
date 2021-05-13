package com.artmaster.android.orthodoxcalendar.domain

import com.artmaster.android.orthodoxcalendar.domain.Fasting.Type
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek
import com.artmaster.android.orthodoxcalendar.domain.Holiday.Month
import com.artmaster.android.orthodoxcalendar.domain.Holiday.MovableDay.*
import org.junit.Assert
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

    @Test
    fun checkFasting() {

        //2021
        var day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 5)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.THURSDAY.num, dayOfMonth = 7)
        checkFastingType(2021, day, Type.NONE)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.THURSDAY.num, dayOfMonth = 8)
        checkFastingType(2021, day, Type.SOLID_WEEK)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 13)
        checkFastingType(2021, day, Type.SOLID_WEEK)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 15)
        checkFastingType(2021, day, Type.SOLID_WEEK)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.MONDAY.num, dayOfMonth = 18)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 20)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.THURSDAY.num, dayOfMonth = 21)
        checkFastingType(2021, day, Type.NONE)

        day = Day(month = Month.FEBRUARY.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 3)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.FEBRUARY.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 24)
        checkFastingType(2021, day, Type.SOLID_WEEK)

        day = Day(month = Month.MARCH.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 3)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.MARCH.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 12)
        checkFastingType(2021, day, Type.SOLID_WEEK)

        day = Day(month = Month.APRIL.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 17)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.APRIL.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 7)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.MAY.num, dayInWeek = DayOfWeek.SATURDAY.num, dayOfMonth = 1)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.MAY.num, dayInWeek = DayOfWeek.SUNDAY.num, dayOfMonth = 2)
        checkFastingType(2021, day, Type.NONE)

        day = Day(month = Month.MAY.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 5)
        checkFastingType(2021, day, Type.SOLID_WEEK)

        day = Day(month = Month.MAY.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 7)
        checkFastingType(2021, day, Type.SOLID_WEEK)

        day = Day(month = Month.MAY.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 14)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.JUNE.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 16)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.JUNE.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 25)
        checkFastingType(2021, day, Type.SOLID_WEEK)

        day = Day(month = Month.JUNE.num, dayInWeek = DayOfWeek.SUNDAY.num, dayOfMonth = 27)
        checkFastingType(2021, day, Type.NONE)

        day = Day(month = Month.JUNE.num, dayInWeek = DayOfWeek.MONDAY.num, dayOfMonth = 28)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.JULY.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 7)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.JULY.num, dayInWeek = DayOfWeek.MONDAY.num, dayOfMonth = 12)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.JULY.num, dayInWeek = DayOfWeek.TUESDAY.num, dayOfMonth = 13)
        checkFastingType(2021, day, Type.NONE)

        day = Day(month = Month.JULY.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 14)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.AUGUST.num, dayInWeek = DayOfWeek.THURSDAY.num, dayOfMonth = 12)
        checkFastingType(2021, day, Type.NONE)

        day = Day(month = Month.AUGUST.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 13)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.AUGUST.num, dayInWeek = DayOfWeek.SATURDAY.num, dayOfMonth = 14)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.AUGUST.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 18)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.AUGUST.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 27)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.AUGUST.num, dayInWeek = DayOfWeek.SATURDAY.num, dayOfMonth = 28)
        checkFastingType(2021, day, Type.NONE)

        day = Day(month = Month.SEPTEMBER.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 10)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.SEPTEMBER.num, dayInWeek = DayOfWeek.SATURDAY.num, dayOfMonth = 11)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.SEPTEMBER.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 22)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.SEPTEMBER.num, dayInWeek = DayOfWeek.MONDAY.num, dayOfMonth = 27)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.SEPTEMBER.num, dayInWeek = DayOfWeek.TUESDAY.num, dayOfMonth = 28)
        checkFastingType(2021, day, Type.NONE)

        day = Day(month = Month.OCTOBER.num, dayInWeek = DayOfWeek.THURSDAY.num, dayOfMonth = 14)
        checkFastingType(2021, day, Type.NONE)

        day = Day(month = Month.NOVEMBER.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 26)
        checkFastingType(2021, day, Type.FASTING_DAY)

        day = Day(month = Month.NOVEMBER.num, dayInWeek = DayOfWeek.SATURDAY.num, dayOfMonth = 27)
        checkFastingType(2021, day, Type.NONE)

        day = Day(month = Month.NOVEMBER.num, dayInWeek = DayOfWeek.SUNDAY.num, dayOfMonth = 28)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.DECEMBER.num, dayInWeek = DayOfWeek.MONDAY.num, dayOfMonth = 1)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.DECEMBER.num, dayInWeek = DayOfWeek.SATURDAY.num, dayOfMonth = 4)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.DECEMBER.num, dayInWeek = DayOfWeek.THURSDAY.num, dayOfMonth = 9)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.DECEMBER.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 17)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.DECEMBER.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 29)
        checkFastingType(2021, day, Type.FASTING)

        day = Day(month = Month.DECEMBER.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 31)
        checkFastingType(2021, day, Type.FASTING)

        //2022
        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 1)
        checkFastingType(2022, day, Type.FASTING)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.WEDNESDAY.num, dayOfMonth = 6)
        checkFastingType(2022, day, Type.FASTING)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.THURSDAY.num, dayOfMonth = 7)
        checkFastingType(2022, day, Type.NONE)

        //2020
        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.MONDAY.num, dayOfMonth = 6)
        checkFastingType(2020, day, Type.FASTING)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.TUESDAY.num, dayOfMonth = 7)
        checkFastingType(2020, day, Type.NONE)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 17)
        checkFastingType(2020, day, Type.SOLID_WEEK)

        day = Day(month = Month.JANUARY.num, dayInWeek = DayOfWeek.SATURDAY.num, dayOfMonth = 18)
        checkFastingType(2020, day, Type.FASTING_DAY)

        day = Day(month = Month.FEBRUARY.num, dayInWeek = DayOfWeek.MONDAY.num, dayOfMonth = 10)
        checkFastingType(2020, day, Type.SOLID_WEEK)

        day = Day(month = Month.FEBRUARY.num, dayInWeek = DayOfWeek.FRIDAY.num, dayOfMonth = 21)
        checkFastingType(2020, day, Type.FASTING_DAY)

        day = Day(month = Month.MARCH.num, dayInWeek = DayOfWeek.SATURDAY.num, dayOfMonth = 1)
        checkFastingType(2020, day, Type.NONE)

        day = Day(month = Month.MARCH.num, dayInWeek = DayOfWeek.MONDAY.num, dayOfMonth = 2)
        checkFastingType(2020, day, Type.FASTING)

        day = Day(month = Month.APRIL.num, dayInWeek = DayOfWeek.SATURDAY.num, dayOfMonth = 18)
        checkFastingType(2020, day, Type.FASTING)

        day = Day(month = Month.APRIL.num, dayInWeek = DayOfWeek.SUNDAY.num, dayOfMonth = 19)
        checkFastingType(2020, day, Type.NONE)

        day = Day(month = Month.JUNE.num, dayInWeek = DayOfWeek.SATURDAY.num, dayOfMonth = 13)
        checkFastingType(2020, day, Type.SOLID_WEEK)

        day = Day(month = Month.JUNE.num, dayInWeek = DayOfWeek.SUNDAY.num, dayOfMonth = 14)
        checkFastingType(2020, day, Type.NONE)

        day = Day(month = Month.JUNE.num, dayInWeek = DayOfWeek.MONDAY.num, dayOfMonth = 15)
        checkFastingType(2020, day, Type.FASTING)
    }

    private fun checkFastingType(year: Int, day: Day, expectedType: Type) {
        day.year = year
        val data = DynamicData()
        data.fillFastingDay(day)
        data.fillOtherData(day)

        Assert.assertEquals("Wrong month", day.fasting.type, expectedType)
    }

    @Test
    fun checkMemorialDays() {
        checkDynamicHoliday(2019, PERSECUTED, Holiday(month = 2, day = 10))
        checkDynamicHoliday(2020, PERSECUTED, Holiday(month = 2, day = 9))
        checkDynamicHoliday(2021, PERSECUTED, Holiday(month = 2, day = 7))
        checkDynamicHoliday(2022, PERSECUTED, Holiday(month = 2, day = 6))
    }

    abstract fun checkDynamicHoliday(year: Int, type: Holiday.MovableDay, expectedHoliday: Holiday)
}
