package com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.HIDE_HORIZONTAL_MONTHS_TAB
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.common.AppBarPreview
import com.artmaster.android.orthodoxcalendar.ui.theme.defaultTileDayInfoSize
import com.artmaster.android.orthodoxcalendar.ui.theme.holidayMonthTabsHeight
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModelFake
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ISettingsViewModel
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.SettingsViewModelFake

@Preview(device = Devices.PIXEL_7A)
@Composable
fun Preview() {
    val model = CalendarViewModelFake()
    val dayOfMonth = remember { mutableIntStateOf(1) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            AppBarPreview()
            HolidayTileMonthLayout(
                data = model.getCurrentMonthData(1),
                dayOfMonth = dayOfMonth.intValue,
                onDayClick = {
                    dayOfMonth.intValue = it.dayOfMonth
                },
                onHolidayClick = {},
                settingsViewModel = SettingsViewModelFake()
            )
        }
    }
}

@Composable
fun HolidayTileMonthLayout(
    modifier: Modifier = Modifier,
    settingsViewModel: ISettingsViewModel,
    data: MutableState<List<Day>>,
    dayOfMonth: Int,
    onDayClick: (day: Day) -> Unit,
    onHolidayClick: (holiday: Holiday) -> Unit,
) {

    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        HolidayTileMonthLayoutLandscape(modifier, data, dayOfMonth, onDayClick, onHolidayClick)
    } else {
        HolidayTileMonthLayoutPortrait(
            modifier,
            settingsViewModel,
            data,
            dayOfMonth,
            onDayClick,
            onHolidayClick
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HolidayTileMonthLayoutPortrait(
    modifier: Modifier = Modifier,
    settingsViewModel: ISettingsViewModel,
    data: MutableState<List<Day>>,
    dayOfMonth: Int,
    onDayClick: (day: Day) -> Unit,
    onHolidayClick: (holiday: Holiday) -> Unit
) {

    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val aspectRatio = screenAspectRatio()
    val state = rememberBottomSheetState(BottomSheetValue.Collapsed)

    // if the aspect ratio is not a square type
    val goodAspectRatio = aspectRatio < 0.55
    // if screen height size is big then make BottomSheetScaffold height longer
    val sheetPeekHeight = if (goodAspectRatio) {
        val hideMenu = !settingsViewModel.getSetting(HIDE_HORIZONTAL_MONTHS_TAB).value.toBoolean()
        (screenHeightDp / 2.5).dp + if (hideMenu) 0.dp else holidayMonthTabsHeight
    } else defaultTileDayInfoSize
    val headerHeight = if (goodAspectRatio) 80.dp else defaultTileDayInfoSize

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
                        headerHeight = headerHeight,
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
    modifier: Modifier,
    data: MutableState<List<Day>>,
    dayOfMonth: Int,
    onDayClick: (day: Day) -> Unit,
    onHolidayClick: (holiday: Holiday) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (data.value.isEmpty()) {
            Spinner()
        } else {
            val day = getDay(data.value, dayOfMonth)
            Row(modifier = Modifier.fillMaxHeight()) {
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