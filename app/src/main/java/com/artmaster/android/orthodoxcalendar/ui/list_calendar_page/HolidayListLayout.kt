package com.artmaster.android.orthodoxcalendar.ui.list_calendar_page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.HIDE_HORIZONTAL_YEARS_TAB
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.list_calendar_page.components.YearsTabs
import com.artmaster.android.orthodoxcalendar.ui.theme.NoRippleTheme
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.HolidayListWrapper
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModelFake
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ICalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ISettingsViewModel
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.SettingsViewModelFake
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Preview
@Composable
fun HolidayListLayoutPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
            HolidayPagerListLayout(
                viewModel = CalendarViewModelFake(),
                settingsViewModel = SettingsViewModelFake(),
                onDayClick = {},
                onEditClick = {},
                onDeleteClick = {},
                onHolidayClick = {})
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HolidayPagerListLayout(
    viewModel: ICalendarViewModel,
    settingsViewModel: ISettingsViewModel,
    onDayClick: (day: Day) -> Unit,
    onEditClick: (holiday: Holiday) -> Unit,
    onDeleteClick: (holiday: Holiday) -> Unit,
    onHolidayClick: (day: Holiday) -> Unit,
) {

    val availableYears = viewModel.getAvailableYears()
    val startIndex = viewModel.getYear().value - availableYears.first()
    val currentYear by viewModel.getYear()

    val pagerState = rememberPagerState(startIndex)
    val scope = rememberCoroutineScope()
    val filters = viewModel.getActiveFilters()

    LaunchedEffect(currentYear) {
        scope.launch {
            val index = currentYear - availableYears.first()
            if (pagerState.currentPage != index) pagerState.scrollToPage(index)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.setYear(availableYears.first() + pagerState.currentPage)
    }

    LaunchedEffect(pagerState, currentYear, filters.value) {

        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->

            val year = availableYears.first() + page

            //current page
            viewModel.loadAllHolidaysOfYear(year)
            //next page data
            viewModel.loadAllHolidaysOfYear(year + 1)
            //previous page data
            viewModel.loadAllHolidaysOfYear(year - 1)
        }
    }

    Column(Modifier.fillMaxHeight()) {
        if (!settingsViewModel.getSetting(HIDE_HORIZONTAL_YEARS_TAB).value.toBoolean()) {
            YearsTabs(pagerState = pagerState) {
                scope.launch {
                    pagerState.scrollToPage(it)
                }
            }
        }

        HorizontalPager(
            count = Constants.HolidayList.PAGE_SIZE.value,
            state = pagerState,
            key = { r -> r }
        ) { page ->
            HolidayListWrapper(
                //modifier = Modifier.graphicsLayer { graphicalLayerTransform(this, pageOffset, pagerState) },
                data = viewModel.getCurrentYearData(yearNum = availableYears.first() + page),
                viewModel = viewModel,
                onDayClick = onDayClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onHolidayClick = onHolidayClick
            )
        }
    }

}