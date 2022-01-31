package com.artmaster.android.orthodoxcalendar.ui.tile_month.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.*
import com.artmaster.android.orthodoxcalendar.domain.Time
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

@Preview
@Composable
fun ShowMonth() {
    val days = ArrayList<Day>()
    val time = Time()
    var dayInWeek = 3
    for (index in 1..time.dayOfMonth) {
        days.add(
            Day(
                year = time.year,
                month = time.month,
                dayOfMonth = index,
                dayInWeek = dayInWeek,
                holidays = arrayListOf(
                    Holiday(
                        year = time.year,
                        month = time.month,
                        day = index,
                        typeId = Holiday.Type.MAIN.id
                    )
                ),
                fasting = Fasting(
                    Fasting.Type.SOLID_WEEK,
                    permissions = listOf(
                        Fasting.Permission.FISH,
                        Fasting.Permission.VINE,
                        Fasting.Permission.OIL,
                        Fasting.Permission.CAVIAR,
                    )
                )
            )
        )
        dayInWeek = if (dayInWeek == SUNDAY.num) MONDAY.num
        else dayInWeek.inc()
    }
    TileMonthGrid(days = days, time = time)
}

@Composable
fun TileMonthGrid(days: List<Day>, time: Time) {
    Row(modifier = Modifier.fillMaxSize()) {
        val currentTime = Time(time.calendar)
        val numDays = currentTime.daysInMonth

        val firstDayInWeek = days.first().dayInWeek
        val maxWeekCount = ceil((firstDayInWeek + numDays) / 7f).toInt()

        var daysCount = 0

        for (currentWeekNum in 1..maxWeekCount) {
            Column(
                modifier = Modifier.fillMaxWidth((1f / (maxWeekCount - currentWeekNum + 1))),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (index in 1..SUNDAY.num) {
                    val curDayNum = ((currentWeekNum - 1) * 7) + index
                    currentTime.calendar.set(Calendar.DAY_OF_MONTH, curDayNum)
                    if (daysCount < days.size && days[daysCount].dayInWeek == index) {
                        MonthDay(days[daysCount])
                        daysCount++
                    } else EmptyDay()
                }
            }
        }
    }
}