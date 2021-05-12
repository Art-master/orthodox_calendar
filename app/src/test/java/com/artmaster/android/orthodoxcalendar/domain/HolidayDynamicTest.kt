package com.artmaster.android.orthodoxcalendar.domain

import org.junit.Assert

class HolidayDynamicTest : HolidayDynamicTestFactory() {
    override fun checkDynamicHoliday(year: Int, type: Holiday.MovableDay, expectedHoliday: Holiday) {
        val holiday = Holiday(dynamicType = type.dynamicType)
        val dynamicHoliday = DynamicData()
        holiday.year = year
        dynamicHoliday.calcHolidayDateIfDynamic(holiday)
        Assert.assertEquals("Wrong day", expectedHoliday.day, holiday.day)
        Assert.assertEquals("Wrong month", expectedHoliday.month, holiday.month)
    }
}
