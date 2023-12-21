package com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.common.Divider
import com.artmaster.android.orthodoxcalendar.ui.common.DividerWithText
import com.artmaster.android.orthodoxcalendar.ui.common.Empty
import com.artmaster.android.orthodoxcalendar.ui.holiday_page.components.HolidayPage
import com.artmaster.android.orthodoxcalendar.ui.theme.Background
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeaderTextColor
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModelFake
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ICalendarViewModel

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun HolidayListPreview() {
    val time = Time()
    val viewModel = CalendarViewModelFake()

    HolidayList(
        data = viewModel.getCurrentYearData(time.year),
        viewModel = CalendarViewModelFake(),
        onHolidayClick = {},
        onDayClick = {})
}

@Preview
@Composable
fun HolidayListOneDayPreview() {
    val viewModel = CalendarViewModelFake()
    OneDayHolidayList(
        day = viewModel.getCurrentMonthData(1).value[0],
        onHolidayClick = {}
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
        ),
        onHolidayClick = {}
    )
}

@Composable
fun HolidayListWrapper(
    modifier: Modifier = Modifier,
    data: MutableState<List<Day>>,
    onDayClick: (day: Day) -> Unit,
    onHolidayClick: (day: Holiday) -> Unit,
    onEditClick: (holiday: Holiday) -> Unit,
    onDeleteClick: (holiday: Holiday) -> Unit,
    viewModel: ICalendarViewModel
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        HolidayListLandscape(modifier, data, onDayClick, onEditClick, onDeleteClick, viewModel)
    } else {
        HolidayList(modifier, data, onDayClick, onHolidayClick, viewModel)
    }
}

@Composable
fun HolidayList(
    modifier: Modifier = Modifier,
    data: MutableState<List<Day>>,
    onDayClick: (day: Day) -> Unit,
    onHolidayClick: (day: Holiday) -> Unit,
    viewModel: ICalendarViewModel
) {
    val listState = rememberLazyListState()
    val dayOfYear = viewModel.getDayOfYear()

    LaunchedEffect(dayOfYear.value) {
        listState.scrollToItem(dayOfYear.value.dec())
    }

    val days = data.value
    val noHolidays = rememberSaveable(days) {
        mutableStateOf(days.isNotEmpty() && !days.any { day -> day.holidays.isNotEmpty() })
    }

    if (noHolidays.value) {
        Empty()
        return
    }

    LazyColumn(modifier = modifier, state = listState) {
        items(days) { day ->
            key(day.month + day.dayInWeek) {
                if (day.holidays.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.0.dp))
                    DayOfWeekTitle(day)
                    ItemHeader(
                        day = day,
                        showDaysOfWeek = false,
                        onClick = onDayClick
                    )
                    Spacer(modifier = Modifier.height(10.0.dp))
                    HolidayList(day = day, onHolidayClick = onHolidayClick)
                }
            }
        }
    }
}

@Composable
fun HolidayListLandscape(
    modifier: Modifier = Modifier,
    data: MutableState<List<Day>>,
    onDayClick: (day: Day) -> Unit,
    onEditClick: (holiday: Holiday) -> Unit,
    onDeleteClick: (holiday: Holiday) -> Unit,
    viewModel: ICalendarViewModel
) {
    val listState = rememberLazyListState()
    val dayOfYear = viewModel.getDayOfYear()

    val holiday = rememberSaveable(data.value) {
        val current = viewModel.getNearestHoliday()
        mutableStateOf(current)
    }
    val onHolidayClick by rememberUpdatedState { h: Holiday ->
        holiday.value = h
    }

    LaunchedEffect(dayOfYear.value) {
        listState.scrollToItem(dayOfYear.value.dec())
    }

    val days = data.value
    val noHolidays = rememberSaveable(days) {
        mutableStateOf(days.isNotEmpty() && !days.any { day -> day.holidays.isNotEmpty() })
    }

    if (noHolidays.value) {
        Empty()
        return
    }

    Row {
        LazyColumn(modifier = modifier.fillMaxWidth(0.5f), state = listState) {
            items(days) { day ->
                key(day.month + day.dayInWeek) {
                    if (day.holidays.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.0.dp))
                        DayOfWeekTitle(day)
                        ItemHeader(
                            day = day,
                            showDaysOfWeek = false,
                            onClick = onDayClick
                        )
                        Spacer(modifier = Modifier.height(10.0.dp))
                        HolidayList(day = day, onHolidayClick = onHolidayClick)
                    }
                }
            }
        }
        holiday.value?.let {
            HolidayPage(
                //modifier = Modifier.graphicsLayer { graphicalLayerTransform(this, pageOffset) },
                viewModel = viewModel,
                holiday = it,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                isHeaderEnable = false,
                titleHeightInitSize = 100
            )
        }

    }
}

@Composable
fun DayOfWeekTitle(day: Day) {
    val name = stringArrayResource(R.array.daysNames)[day.dayInWeek.dec()]
    val textColor = if (day.dayInWeek == Holiday.DayOfWeek.SUNDAY.num) {
        HeadSymbolTextColor
    } else HeaderTextColor
    DividerWithText {
        Text(
            modifier = Modifier
                .background(Background)
                .padding(start = 3.dp, end = 3.dp)
                .offset(y = 6.dp),
            color = textColor,
            text = name,
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OneDayHolidayList(
    day: Day,
    headerHeight: Dp = 93.dp,
    onHolidayClick: (holiday: Holiday) -> Unit
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth(if (isLandscape) 0.8f else 1f)
            .fillMaxHeight(if (isLandscape) 1f else 0.8f)
            .padding(top = if (isLandscape) 0.dp else 15.dp)
    ) {

        if (isLandscape.not()) {
            ItemHeader(day = day, showDaysOfWeek = true, headerHeight = headerHeight)
            Divider()
        }

        Column(
            modifier = Modifier
                .verticalScroll(scroll)
                .fillMaxSize()
                .padding(top = if (isLandscape) 0.dp else 10.dp)
        ) {
            if (isLandscape) { // if landscape orientation than move header into scroller
                ItemHeader(day = day, showDaysOfWeek = true)
                Divider()
            }
            if (day.holidays.isNotEmpty()) {
                HolidayList(day = day, onHolidayClick = onHolidayClick)
            } else NoItems()
        }
        Spacer(modifier = Modifier.height(20.0.dp))
    }
}

@Composable
fun HolidayList(day: Day, onHolidayClick: (holiday: Holiday) -> Unit) {
    day.holidays.forEach {
        //AnimatedVisibility(visible = true)
        HolidayItem(holiday = it, onClick = onHolidayClick)
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