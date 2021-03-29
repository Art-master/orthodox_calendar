package com.artmaster.android.orthodoxcalendar.domain

import org.junit.Assert

class HolidayDynamicTest : HolidayDynamicTestFactory() {
    override fun checkHoliday(holiday: Holiday, expectedDay: Int, expectedMonth: Int) {
        val dynamicHoliday = DynamicData(holiday.year)
        dynamicHoliday.fillHoliday(holiday)
        Assert.assertEquals(holiday.day.toLong(), expectedDay.toLong())
        Assert.assertEquals(holiday.month.toLong(), expectedMonth.toLong())
    }
}
