package com.artmaster.android.orthodoxcalendar.domain

import org.junit.Assert

class HolidayDynamicTest : HolidayDynamicTestFactory() {
    override fun checkDynamicHoliday(year: Int, type: Holiday.MovableDay, expectedHoliday: Holiday) {
        val holiday = Holiday(dynamicType = type.dynamicType)
        val dynamicHoliday = DynamicData()
        holiday.year = year
        dynamicHoliday.fillHoliday(holiday)
        Assert.assertEquals("Wrong year", holiday.year, expectedHoliday.year)
        Assert.assertEquals("Wrong day", holiday.day, expectedHoliday.day)
        Assert.assertEquals("Wrong month", holiday.month, expectedHoliday.month)
    }
}
