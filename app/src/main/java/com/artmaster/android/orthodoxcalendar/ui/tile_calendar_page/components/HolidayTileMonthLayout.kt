package com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices.PIXEL_3
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time

@Preview(device = PIXEL_3)
@Composable
fun Preview() {
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
                        typeId = Holiday.Type.MAIN.id
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
        dayInWeek = if (dayInWeek == Holiday.DayOfWeek.SUNDAY.num) Holiday.DayOfWeek.MONDAY.num
        else dayInWeek.inc()
    }

    val monthData = remember {
        mutableStateOf(emptyList<Day>())
    }

    HolidayTileMonthLayout(data = monthData, dayOfMonth = 4)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HolidayTileMonthLayout(
    modifier: Modifier = Modifier,
    data: MutableState<List<Day>>,
    dayOfMonth: Int,
    onDayClick: (day: Day) -> Unit = {},
    onHolidayClick: (holiday: Holiday) -> Unit = {}
) {

    val days = data.value

    val state = rememberDrawerState(DrawerValue.Closed)
    val drawerState = rememberBottomSheetScaffoldState(state)

    val sheetPeekHeight = 100.dp

    Column(
        modifier = Modifier.padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (days.isEmpty()) {
            Spinner()
        } else {
            val day = data.value[dayOfMonth.dec()]

            BottomSheetScaffold(
                modifier = modifier,
                scaffoldState = drawerState,
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
                TilesGridLayout(days, dayOfMonth, onDayClick)
            }
        }
    }
}