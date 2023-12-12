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
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModelFake

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun ShowMonth() {
    val model = CalendarViewModelFake()
    val time = Time()

    TilesGridLayout(days = model.getCurrentMonthData(1), selectedDayOfMonth = time.dayOfMonth)
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
            .fillMaxWidth(0.45f)
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

            key(dayIndex) {
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
}
