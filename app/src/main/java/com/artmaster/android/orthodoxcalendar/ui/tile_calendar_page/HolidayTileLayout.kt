package com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_COUNT
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.HolidayTileMonthLayout
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.MonthTabs
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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
    onDayClick: (day: Day) -> Unit = {},
    onHolidayClick: (day: Holiday) -> Unit = {},
) {
    val monthNum by viewModel.getMonth()
    val currentYear by viewModel.getYear()

    val pagerState = rememberPagerState(monthNum)
    val scope = rememberCoroutineScope()
    val filters = viewModel.getActiveFilters()

    LaunchedEffect(monthNum) {
        scope.launch {
            pagerState.scrollToPage(monthNum)
        }
    }

    LaunchedEffect(pagerState.currentPage, currentYear, filters.value) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.setMonth(page)

            //current page
            viewModel.loadAllHolidaysOfMonth(page, currentYear)
            //next page data
            viewModel.loadAllHolidaysOfMonth(page + 1, currentYear)
            //previous page data
            viewModel.loadAllHolidaysOfMonth(page - 1, currentYear)
        }
    }

    val onTabClick = remember {
        { page: Int ->
            scope.launch {
                pagerState.scrollToPage(page)
            }
            Unit
        }
    }

    Column(Modifier.fillMaxHeight()) {
        MonthTabs(
            currentPage = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            },
            onClick = onTabClick
        )

        HorizontalPager(
            count = MONTH_COUNT,
            state = pagerState,
            key = { r -> r }
        ) { page ->
            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

            if (needToShowLayout(pageOffset)) {
                HolidayTileMonthLayout(
                    data = viewModel.getCurrentMonthData(monthNum = page),
                    dayOfMonth = viewModel.getDayOfMonth().value,
                    onDayClick = onDayClick,
                    onHolidayClick = onHolidayClick
                )
            }
        }
    }
}

fun needToShowLayout(pageOffset: Float) = pageOffset < 0.8f
