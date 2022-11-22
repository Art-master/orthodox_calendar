package com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.common.Divider
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
            Fasting.Type.FASTING_DAY,
            permissions = listOf(
                Fasting.Permission.FISH,
                Fasting.Permission.VINE,
                Fasting.Permission.OIL,
                Fasting.Permission.CAVIAR,
            )
        )
    )
    day.holidays.addAll(getHolidays(time))
    val data = remember { mutableStateOf(listOf(day)) }

    HolidayList(data = data)
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
                Fasting.Type.FASTING_DAY,
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
                Fasting.Type.GREAT_FASTING,
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
fun HolidayList(
    modifier: Modifier = Modifier,
    headerHeight: Dp = 90.dp,
    data: MutableState<List<Day>>,
    onDayClick: (day: Day) -> Unit = {},
    onHolidayClick: (day: Holiday) -> Unit = {},
    viewModel: CalendarViewModel? = null
) {
    val listState = rememberLazyListState()
    val dayOfYear = viewModel?.getDayOfYear()

    LaunchedEffect(dayOfYear?.value) {
        listState.scrollToItem(dayOfYear!!.value.dec())
    }

    val days = data.value
    LazyColumn(modifier = modifier, state = listState) {
        items(days) { day ->
            if (day.holidays.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.0.dp))
                ItemHeader(
                    day = day,
                    showDaysOfWeek = true,
                    headerHeight = headerHeight,
                    onClick = onDayClick
                )
                day.holidays.forEach {
                    HolidayItem(holiday = it, onClick = onHolidayClick)
                }
                Divider()
            }
        }
    }
}

@Composable
fun OneDayHolidayList(
    day: Day,
    headerHeight: Dp = 93.dp,
    onHolidayClick: (holiday: Holiday) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(top = 15.dp)
    ) {

        ItemHeader(day = day, showDaysOfWeek = true, headerHeight = headerHeight)

        Divider()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 25.dp)
        ) {
            if (day.holidays.isNotEmpty()) {
                day.holidays.forEach {
                    //AnimatedVisibility(visible = true)
                    HolidayItem(holiday = it, onClick = onHolidayClick)
                }
            } else NoItems()
        }
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