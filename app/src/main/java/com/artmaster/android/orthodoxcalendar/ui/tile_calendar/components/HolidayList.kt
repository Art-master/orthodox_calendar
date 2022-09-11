package com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time

@Preview
@Composable
fun HolidayListPreview() {
    val time = Time()
    val dayInWeek = 3

    val day = Day(
        year = time.year,
        month = time.month,
        dayOfMonth = time.dayOfMonth,
        dayInWeek = dayInWeek,
        holidays = arrayListOf(),
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
    day.holidays.addAll(getHolidays(time))
    HolidayList(data = day)
}

fun getHolidays(time: Time = Time()): List<Holiday> {
    return (0..6).map { index ->
        Holiday(
            title = "Праздник",
            year = time.year,
            month = time.month,
            day = time.dayOfMonth,
            typeId = if (index % 4 == 0) Holiday.Type.TWELVE_MOVABLE.id
            else Holiday.Type.AVERAGE_PEPPY.id
        )
    }
}

@Preview
@Composable
fun HolidayListItemPreview() {
    val time = Time()
    OneDayHolidayList(
        day = Day(
            year = time.year,
            month = time.month,
            dayOfMonth = time.dayOfMonth,
            dayInWeek = time.dayOfWeek,
            holidays = getHolidays(time) as ArrayList<Holiday>,
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

@Composable
fun HolidayList(data: Day) {

    val holidays: List<Holiday> = data.holidays

    LazyColumn {
        items(holidays.size) { index ->
            HolidayItem(holidays[index])
        }
    }
}

@Composable
fun OneDayHolidayList(
    day: Day,
    headerHeight: Dp = 56.dp,
    onClickHoliday: (holiday: Holiday) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(top = 15.dp)
    ) {

        ItemHeader(day = day, headerHeight)

        day.holidays.forEach {
//      AnimatedVisibility(visible = true)
            HolidayItem(holiday = it, onClick = onClickHoliday)
        }
    }
}