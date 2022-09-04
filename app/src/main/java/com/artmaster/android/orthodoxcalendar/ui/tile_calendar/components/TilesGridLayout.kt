package com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.MONDAY
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.SUNDAY
import com.artmaster.android.orthodoxcalendar.domain.Time

@Preview
@Composable
fun ShowMonth() {
    val days = ArrayList<Day>()
    val time = Time()
    var dayInWeek = 3
    for (index in 1..time.daysInMonth) {
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
                        typeId = if (index % 4 == 0) Holiday.Type.TWELVE_MOVABLE.id
                        else Holiday.Type.AVERAGE_PEPPY.id
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

    val data = remember {
        mutableStateOf(days.toList())
    }
    TilesGridLayout(data = data, selectedDayOfMonth = time.dayOfMonth)
}

@Composable
fun TilesGridLayout(
    data: MutableState<List<Day>>,
    selectedDayOfMonth: Int,
    onDayClick: (day: Day) -> Unit = {}
) {

    Row(modifier = Modifier.fillMaxSize()) {
        Grid(data = data, selectedDayOfMonth = selectedDayOfMonth, onDayClick = onDayClick)
    }
}

const val MAX_COLUMN_COUNT = 6

fun calculateColumnFraction(currentColumnNum: Int): Float {
    return (1f / (MAX_COLUMN_COUNT - currentColumnNum + 1))
}


@Composable
fun Grid(
    data: MutableState<List<Day>>,
    selectedDayOfMonth: Int,
    onDayClick: (day: Day) -> Unit = {},
) {

    val days = data.value

    var daysCount = 0

    for (currentColumnNum in 0..MAX_COLUMN_COUNT) {
        key(currentColumnNum) {
            Column(
                modifier = Modifier.fillMaxWidth(calculateColumnFraction(currentColumnNum)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (index in 1..SUNDAY.num) {
                    if (currentColumnNum == 0) {
                        DayOfWeekName(dayOfWeekNum = index)
                        continue
                    }

                    if (daysCount < days.size && days[daysCount].dayInWeek == index) {

                        daysCount++
                        val isActive = daysCount == selectedDayOfMonth

                        DayOfMonthTile(
                            day = days[daysCount.dec()],
                            isActive = isActive,
                            onClick = onDayClick
                        )

                    } else EmptyInvisibleTile()
                }
            }
        }
    }
}
