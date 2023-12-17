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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_COUNT
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.HIDE_HORIZONTAL_MONTHS_TAB
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.HolidayTileMonthLayout
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.MonthTabs
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModelFake
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ICalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ISettingsViewModel
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.SettingsViewModelFake
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun PreviewLayout() {
    HolidayTileLayout(
        viewModel = CalendarViewModelFake(),
        settingsViewModel = SettingsViewModelFake(),
        onDayClick = {},
        onHolidayClick = {})
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HolidayTileLayout(
    viewModel: ICalendarViewModel,
    settingsViewModel: ISettingsViewModel,
    onDayClick: (day: Day) -> Unit,
    onHolidayClick: (day: Holiday) -> Unit,
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
        if (!settingsViewModel.getSetting(HIDE_HORIZONTAL_MONTHS_TAB).value.toBoolean()) {
            MonthTabs(
                currentPage = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                },
                onClick = onTabClick
            )
        }

        HorizontalPager(
            count = MONTH_COUNT,
            state = pagerState
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
