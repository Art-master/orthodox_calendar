package com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModelFake

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun Preview() {
    val model = CalendarViewModelFake();

    HolidayTileMonthLayout(
        data = model.getCurrentMonthData(1),
        dayOfMonth = 4,
        onHolidayClick = {},
        onDayClick = {})
}

@Composable
fun HolidayTileMonthLayout(
    modifier: Modifier = Modifier,
    data: MutableState<List<Day>>,
    dayOfMonth: Int,
    onDayClick: (day: Day) -> Unit,
    onHolidayClick: (holiday: Holiday) -> Unit
) {

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        HolidayTileMonthLayoutLandscape(modifier, data, dayOfMonth, onDayClick, onHolidayClick)
    } else {
        HolidayTileMonthLayoutPortrait(modifier, data, dayOfMonth, onDayClick, onHolidayClick)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HolidayTileMonthLayoutPortrait(
    modifier: Modifier = Modifier,
    data: MutableState<List<Day>>,
    dayOfMonth: Int,
    onDayClick: (day: Day) -> Unit,
    onHolidayClick: (holiday: Holiday) -> Unit
) {

    val state = rememberBottomSheetState(BottomSheetValue.Collapsed)

    val sheetPeekHeight = 120.dp

    Column(
        modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (data.value.isEmpty()) {
            Spinner()
        } else {
            val day = getDay(data.value, dayOfMonth)
            BottomSheetScaffold(
                modifier = modifier,
                scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = state),
                sheetContent = {
                    OneDayHolidayList(
                        day = day,
                        headerHeight = sheetPeekHeight,
                        onHolidayClick = onHolidayClick
                    )
                },
                sheetShape = RoundedCornerShape(5.dp),
                sheetElevation = 8.dp,
                backgroundColor = Color.Transparent,
                sheetPeekHeight = sheetPeekHeight

            ) {
                TilesGridLayout(data, day.dayOfMonth, onDayClick)
            }
        }
    }
}

@Composable
fun HolidayTileMonthLayoutLandscape(
    modifier: Modifier = Modifier,
    data: MutableState<List<Day>>,
    dayOfMonth: Int,
    onDayClick: (day: Day) -> Unit,
    onHolidayClick: (holiday: Holiday) -> Unit
) {
    Column(
        modifier = Modifier.padding(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (data.value.isEmpty()) {
            Spinner()
        } else {
            val day = getDay(data.value, dayOfMonth)
            Row {
                TilesGridLayout(data, day.dayOfMonth, onDayClick)
                Spacer(modifier = Modifier.height(10.0.dp))
                OneDayHolidayList(
                    day = day,
                    onHolidayClick = onHolidayClick
                )
            }
        }
    }
}

fun getDay(data: List<Day>, dayOfMonth: Int): Day {
    return if (data.size > dayOfMonth.dec()) data[dayOfMonth.dec()] else data.last()
}