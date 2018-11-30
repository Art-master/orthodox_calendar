package com.artmaster.android.orthodoxcalendar

import com.artmaster.android.orthodoxcalendar.domain.HolidayDynamicTest
import com.artmaster.android.orthodoxcalendar.domain.TimeTest

import junit.framework.TestSuite

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(HolidayDynamicTest::class, TimeTest::class)
class OrthodoxCalendarTests : TestSuite()
