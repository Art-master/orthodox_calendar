package com.artmaster.android.orthodoxcalendar.domain

import com.artmaster.android.orthodoxcalendar.domain.Fasting.Type
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.FRIDAY
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.MONDAY
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.SATURDAY
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.SUNDAY
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.THURSDAY
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.TUESDAY
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.WEDNESDAY
import com.artmaster.android.orthodoxcalendar.domain.Holiday.Month
import com.artmaster.android.orthodoxcalendar.domain.Holiday.Month.JANUARY
import com.artmaster.android.orthodoxcalendar.domain.Holiday.MovableDay.PERSECUTED
import com.artmaster.android.orthodoxcalendar.domain.Holiday.MovableDay.THE_ASCENSION_OF_THE_LORD
import com.artmaster.android.orthodoxcalendar.domain.Holiday.MovableDay.THE_EASTER
import com.artmaster.android.orthodoxcalendar.domain.Holiday.MovableDay.THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM
import com.artmaster.android.orthodoxcalendar.domain.Holiday.MovableDay.THE_HOLY_TRINITY
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

        checkDynamicHoliday(
            2009,
            THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM,
            Holiday(month = 4, day = 12)
        )
        checkDynamicHoliday(2015, THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM, Holiday(month = 4, day = 5))
        checkDynamicHoliday(
            2019,
            THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM,
            Holiday(month = 4, day = 21)
        )

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
        var day = Day(year = 2021, month = JANUARY.num, dayInWeek = FRIDAY.num, dayOfMonth = 5)
        checkFastingType(day, Type.CHRISTMAS_FASTING)

        day = Day(year = 2021, month = JANUARY.num, dayInWeek = THURSDAY.num, dayOfMonth = 7)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2021, month = JANUARY.num, dayInWeek = THURSDAY.num, dayOfMonth = 8)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2021, month = JANUARY.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 13)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2021, month = JANUARY.num, dayInWeek = FRIDAY.num, dayOfMonth = 15)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2021, month = JANUARY.num, dayInWeek = MONDAY.num, dayOfMonth = 18)
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(year = 2021, month = JANUARY.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 20)
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(year = 2021, month = JANUARY.num, dayInWeek = THURSDAY.num, dayOfMonth = 21)
        checkFastingType(day, Type.NONE)

        day =
            Day(year = 2021, month = Month.FEBRUARY.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 3)
        checkFastingType(day, Type.FASTING_DAY)

        day =
            Day(year = 2021, month = Month.FEBRUARY.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 24)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2021, month = Month.MARCH.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 3)
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(year = 2021, month = Month.MARCH.num, dayInWeek = FRIDAY.num, dayOfMonth = 12)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2021, month = Month.APRIL.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 17)
        checkFastingType(day, Type.GREAT_FASTING)

        day = Day(year = 2021, month = Month.APRIL.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 7)
        checkFastingType(day, Type.GREAT_FASTING)

        day = Day(year = 2021, month = Month.MAY.num, dayInWeek = SATURDAY.num, dayOfMonth = 1)
        checkFastingType(day, Type.GREAT_FASTING)

        day = Day(year = 2021, month = Month.MAY.num, dayInWeek = SUNDAY.num, dayOfMonth = 2)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2021, month = Month.MAY.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 5)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2021, month = Month.MAY.num, dayInWeek = FRIDAY.num, dayOfMonth = 7)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2021, month = Month.MAY.num, dayInWeek = FRIDAY.num, dayOfMonth = 14)
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(year = 2021, month = Month.JUNE.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 16)
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(year = 2021, month = Month.JUNE.num, dayInWeek = FRIDAY.num, dayOfMonth = 25)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2021, month = Month.JUNE.num, dayInWeek = SUNDAY.num, dayOfMonth = 27)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2021, month = Month.JUNE.num, dayInWeek = MONDAY.num, dayOfMonth = 28)
        checkFastingType(day, Type.PETER_AND_PAUL_FASTING)

        day = Day(year = 2021, month = Month.JULY.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 7)
        checkFastingType(day, Type.PETER_AND_PAUL_FASTING)

        day = Day(year = 2021, month = Month.JULY.num, dayInWeek = MONDAY.num, dayOfMonth = 12)
        checkFastingType(day, Type.PETER_AND_PAUL_FASTING)

        day = Day(year = 2021, month = Month.JULY.num, dayInWeek = TUESDAY.num, dayOfMonth = 13)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2021, month = Month.JULY.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 14)
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(year = 2021, month = Month.AUGUST.num, dayInWeek = THURSDAY.num, dayOfMonth = 12)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2021, month = Month.AUGUST.num, dayInWeek = FRIDAY.num, dayOfMonth = 13)
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(year = 2021, month = Month.AUGUST.num, dayInWeek = SATURDAY.num, dayOfMonth = 14)
        checkFastingType(day, Type.ASSUMPTION_FASTING)

        day = Day(year = 2021, month = Month.AUGUST.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 18)
        checkFastingType(day, Type.ASSUMPTION_FASTING)

        day = Day(year = 2021, month = Month.AUGUST.num, dayInWeek = FRIDAY.num, dayOfMonth = 27)
        checkFastingType(day, Type.ASSUMPTION_FASTING)

        day = Day(year = 2021, month = Month.AUGUST.num, dayInWeek = SATURDAY.num, dayOfMonth = 28)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2021, month = Month.SEPTEMBER.num, dayInWeek = FRIDAY.num, dayOfMonth = 10)
        checkFastingType(day, Type.FASTING_DAY)

        day =
            Day(year = 2021, month = Month.SEPTEMBER.num, dayInWeek = SATURDAY.num, dayOfMonth = 11)
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(
            year = 2021,
            month = Month.SEPTEMBER.num,
            dayInWeek = WEDNESDAY.num,
            dayOfMonth = 22
        )
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(year = 2021, month = Month.SEPTEMBER.num, dayInWeek = MONDAY.num, dayOfMonth = 27)
        checkFastingType(day, Type.FASTING_DAY)

        day =
            Day(year = 2021, month = Month.SEPTEMBER.num, dayInWeek = TUESDAY.num, dayOfMonth = 28)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2021, month = Month.OCTOBER.num, dayInWeek = THURSDAY.num, dayOfMonth = 14)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2021, month = Month.NOVEMBER.num, dayInWeek = FRIDAY.num, dayOfMonth = 26)
        checkFastingType(day, Type.FASTING_DAY)

        day =
            Day(year = 2021, month = Month.NOVEMBER.num, dayInWeek = SATURDAY.num, dayOfMonth = 27)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2021, month = Month.NOVEMBER.num, dayInWeek = SUNDAY.num, dayOfMonth = 28)
        checkFastingType(day, Type.CHRISTMAS_FASTING)

        day = Day(year = 2021, month = Month.DECEMBER.num, dayInWeek = MONDAY.num, dayOfMonth = 1)
        checkFastingType(day, Type.CHRISTMAS_FASTING)

        day = Day(year = 2021, month = Month.DECEMBER.num, dayInWeek = SATURDAY.num, dayOfMonth = 4)
        checkFastingType(day, Type.CHRISTMAS_FASTING)

        day = Day(year = 2021, month = Month.DECEMBER.num, dayInWeek = THURSDAY.num, dayOfMonth = 9)
        checkFastingType(day, Type.CHRISTMAS_FASTING)

        day = Day(year = 2021, month = Month.DECEMBER.num, dayInWeek = FRIDAY.num, dayOfMonth = 17)
        checkFastingType(day, Type.CHRISTMAS_FASTING)

        day =
            Day(year = 2021, month = Month.DECEMBER.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 29)
        checkFastingType(day, Type.CHRISTMAS_FASTING)

        day = Day(year = 2021, month = Month.DECEMBER.num, dayInWeek = FRIDAY.num, dayOfMonth = 31)
        checkFastingType(day, Type.CHRISTMAS_FASTING)

        //2022
        day = Day(year = 2022, month = JANUARY.num, dayInWeek = FRIDAY.num, dayOfMonth = 1)
        checkFastingType(day, Type.CHRISTMAS_FASTING)

        day = Day(year = 2022, month = JANUARY.num, dayInWeek = WEDNESDAY.num, dayOfMonth = 6)
        checkFastingType(day, Type.CHRISTMAS_FASTING)

        day = Day(year = 2022, month = JANUARY.num, dayInWeek = THURSDAY.num, dayOfMonth = 7)
        checkFastingType(day, Type.NONE)

        //2020
        day = Day(year = 2020, month = JANUARY.num, dayInWeek = MONDAY.num, dayOfMonth = 6)
        checkFastingType(day, Type.CHRISTMAS_FASTING)

        day = Day(year = 2020, month = JANUARY.num, dayInWeek = TUESDAY.num, dayOfMonth = 7)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2020, month = JANUARY.num, dayInWeek = FRIDAY.num, dayOfMonth = 17)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2020, month = JANUARY.num, dayInWeek = SATURDAY.num, dayOfMonth = 18)
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(year = 2020, month = Month.FEBRUARY.num, dayInWeek = MONDAY.num, dayOfMonth = 10)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2020, month = Month.FEBRUARY.num, dayInWeek = FRIDAY.num, dayOfMonth = 21)
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(year = 2020, month = Month.MARCH.num, dayInWeek = SATURDAY.num, dayOfMonth = 1)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2020, month = Month.MARCH.num, dayInWeek = MONDAY.num, dayOfMonth = 2)
        checkFastingType(day, Type.GREAT_FASTING)

        day = Day(year = 2020, month = Month.APRIL.num, dayInWeek = SATURDAY.num, dayOfMonth = 18)
        checkFastingType(day, Type.GREAT_FASTING)

        day = Day(year = 2020, month = Month.APRIL.num, dayInWeek = SUNDAY.num, dayOfMonth = 19)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2020, month = Month.JUNE.num, dayInWeek = SATURDAY.num, dayOfMonth = 13)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2020, month = Month.JUNE.num, dayInWeek = SUNDAY.num, dayOfMonth = 14)
        checkFastingType(day, Type.NONE)

        day = Day(year = 2020, month = Month.JUNE.num, dayInWeek = MONDAY.num, dayOfMonth = 15)
        checkFastingType(day, Type.PETER_AND_PAUL_FASTING)
    }

    @Test
    fun checkPeterAndPaulFasting() {
        // 2024
        var day = Day(year = 2024, month = Month.JUNE.num, dayInWeek = SUNDAY.num, dayOfMonth = 23)
        checkFastingType(day, Type.NONE)

        //24-29 June - solid week
        day = Day(year = 2024, month = Month.JUNE.num, dayInWeek = MONDAY.num, dayOfMonth = 24)
        checkFastingType(day, Type.SOLID_WEEK)

        day = Day(year = 2024, month = Month.JUNE.num, dayInWeek = SATURDAY.num, dayOfMonth = 29)
        checkFastingType(day, Type.SOLID_WEEK)

        //1-12 July - fasting
        day = Day(year = 2024, month = Month.JULY.num, dayInWeek = MONDAY.num, dayOfMonth = 1)
        checkFastingType(day, Type.PETER_AND_PAUL_FASTING)

        day = Day(year = 2024, month = Month.JULY.num, dayInWeek = THURSDAY.num, dayOfMonth = 11)
        checkFastingType(day, Type.PETER_AND_PAUL_FASTING)

        day = Day(year = 2024, month = Month.JULY.num, dayInWeek = FRIDAY.num, dayOfMonth = 12)
        checkFastingType(day, Type.FASTING_DAY)

        day = Day(year = 2024, month = Month.JULY.num, dayInWeek = SATURDAY.num, dayOfMonth = 13)
        checkFastingType(day, Type.NONE)
    }

    private fun checkFastingType(day: Day, expectedType: Type) {
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
