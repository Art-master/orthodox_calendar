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
import com.artmaster.android.orthodoxcalendar.domain.Time
import java.util.*

@Preview
@Composable
fun ShowMonth() {
    val days = ArrayList<Day>()
    for (index in 1..31) {
        days.add(
            Day(
                year = 2022,
                month = 1,
                dayOfMonth = index,
                dayInWeek = 5,
                holidays = arrayListOf(
                    Holiday(year = 2022, month = 1, day = index, typeId = Holiday.Type.MAIN.id)
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
    }
    TileMonthGrid(days = days, time = Time())
}

@Composable
fun TileMonthGrid(days: List<Day>, time: Time) {
    Row(modifier = Modifier.fillMaxSize()) {
        val currentTime = Time(time.calendar)
        val numDays = currentTime.daysInMonth
        val weekCount = numDays / 7
        for (currentWeekNum in 1..weekCount) {
            Column(
                modifier = Modifier.fillMaxWidth((1f / (weekCount - currentWeekNum + 1))),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (index in 1..7) {
                    val curDayNum = ((currentWeekNum - 1) * 7) + index
                    currentTime.calendar.set(Calendar.DAY_OF_MONTH, curDayNum)
                    val dayOfWeek = currentTime.dayOfWeek
                    val week = currentTime.calendar.get(Calendar.WEEK_OF_MONTH)
                    MonthDay(days[curDayNum - 1])
                }
            }
        }
    }
}