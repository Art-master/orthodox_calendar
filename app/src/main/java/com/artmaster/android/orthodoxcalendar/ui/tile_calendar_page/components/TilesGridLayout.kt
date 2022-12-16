package com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    val data = remember { mutableStateOf(days.toList()) }
    TilesGridLayout(days = data, selectedDayOfMonth = time.dayOfMonth)
}

const val MAX_COLUMN_COUNT = 7
const val DAYS_IN_WEEK_COUNT = 7

@Composable
fun TilesGridLayout(
    days: MutableState<List<Day>>,
    selectedDayOfMonth: Int,
    onDayClick: (day: Day) -> Unit = {}
) {

    var daysCount = 0
    val data = days.value

    if (data.isEmpty()) {
        Spinner()
        return
    }

    val daysCountStartOffset = data.first().dayInWeek.dec()
    val configuration = LocalConfiguration.current

    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LazyVerticalGrid(
        modifier = if (isLandscape) Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.4f)
        else Modifier.fillMaxWidth(),

        columns = GridCells.Fixed(MAX_COLUMN_COUNT),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(1.dp)
    ) {
        items(MAX_COLUMN_COUNT * DAYS_IN_WEEK_COUNT) { item ->

            val week = item % MAX_COLUMN_COUNT
            val dayWith0 = item / MAX_COLUMN_COUNT
            val dayIndex = ((week.dec() * DAYS_IN_WEEK_COUNT) + dayWith0) - daysCountStartOffset

            if (week == 0) {
                DayOfWeekName(dayOfWeekNum = (dayWith0).inc())

            } else if (dayIndex >= 0 && dayIndex.inc() <= data.size) {
                daysCount++
                val isActive = dayIndex.inc() == selectedDayOfMonth

                DayOfMonthTile(
                    day = data[dayIndex],
                    isActive = isActive,
                    onClick = onDayClick
                )
            }
        }
    }
}
