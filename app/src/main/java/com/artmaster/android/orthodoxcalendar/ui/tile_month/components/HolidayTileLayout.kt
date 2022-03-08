package com.artmaster.android.orthodoxcalendar.ui.tile_month.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect

@Preview
@Composable
fun PreviewLayout() {
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

    val monthData = HashMap<Int, MutableLiveData<List<Day>>>()
    monthData[1] = MutableLiveData(days)

    //HolidayTileLayout(data = monthData, time = time)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HolidayTileLayout(
    viewModel: CalendarViewModel,
    time: Time,
    onClick: (holiday: Holiday) -> Unit = {}
) {
    var monthNum by remember { mutableStateOf(time.month) }

    val pagerState = rememberPagerState(monthNum)

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.setMonth(page.inc())
            monthNum = page.inc()
            viewModel.loadMonthData(page.inc(), time.year)
        }
    }

    Column(Modifier.fillMaxHeight()) {
        val monthCount = 12
        MonthDropdown(monthNum) {
            monthNum = it
        }
        Row {
            HorizontalPager(count = monthCount, state = pagerState) { page ->
                val days = viewModel.getCurrentMonthData(monthNum = page)
                HolidayTileMonthLayout(data = days, time = time)
            }
        }
    }
}

@Composable
fun MonthDropdown(monthNum: Int, onClick: (month: Int) -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    val items = stringArrayResource(id = R.array.months_names_gen)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            items[monthNum.dec()], modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(
                    Color.Gray
                )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.Red
                )
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    onClick.invoke(index.inc())
                    expanded = false
                }) {
                    Text(text = s)
                }
            }
        }
    }
}