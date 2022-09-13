package com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor

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
            Fasting.Type.NONE,
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
fun HolidayListOneDayPreview() {
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

@Preview
@Composable
fun HolidayListOneDayWithoutHolidaysPreview() {
    val time = Time()
    OneDayHolidayList(
        day = Day(
            year = time.year,
            month = time.month,
            dayOfMonth = time.dayOfMonth,
            dayInWeek = time.dayOfWeek,
            holidays = ArrayList(),
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

        HolidaysDivider()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 25.dp)
        ) {
            if (day.holidays.isNotEmpty()) {
                day.holidays.forEach {
                    //AnimatedVisibility(visible = true)
                    HolidayItem(holiday = it, onClick = onClickHoliday)
                }
            } else NoItems()
        }
    }
}

@Composable
fun HolidaysDivider() {
    Row(
        modifier = Modifier.padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            color = DefaultTextColor,
            text = stringResource(id = R.string.ornament_for_name_date_left),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ornament)),
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .offset(y = 3.dp)
                .background(DefaultTextColor)
        )
        Text(
            color = DefaultTextColor,
            text = stringResource(id = R.string.ornament_for_name_date_right),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ornament)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NoItems() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize())
    {
        Text(
            modifier = Modifier.fillMaxWidth(),
            color = DefaultTextColor,
            text = stringResource(id = R.string.no_holidays),
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal)),
            textAlign = TextAlign.Center
        )
    }
}