package com.artmaster.android.orthodoxcalendar.domain

import org.junit.Assert

class HolidayDynamicTest : HolidayDynamicTestFactory() {
    override fun checkHoliday(year: Int, holidayName: String, expectedDay: Int, expectedMonth: Int) {
        val dynamicHoliday = DynamicData(year, holidayName)
        Assert.assertEquals(dynamicHoliday.day.toLong(), expectedDay.toLong())
        Assert.assertEquals(dynamicHoliday.month.toLong(), expectedMonth.toLong())
    }
}
