package com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_COUNT
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.HIDE_HORIZONTAL_MONTHS_TAB
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.common.AppBarPreview
import com.artmaster.android.orthodoxcalendar.ui.common.isLowPerformanceDevice
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.HolidayTileMonthLayout
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.MonthTabs
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModelFake
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ICalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ISettingsViewModel
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.SettingsViewModelFake
import kotlinx.coroutines.launch

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun PreviewLayout() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            AppBarPreview()
            HolidayTileLayout(
                viewModel = viewModel<CalendarViewModelFake>(),
                settingsViewModel = viewModel<SettingsViewModelFake>(),
                onDayClick = {},
                onHolidayClick = {})
        }
    }
}

@Composable
fun HolidayTileLayout(
    viewModel: ICalendarViewModel,
    settingsViewModel: ISettingsViewModel,
    onDayClick: (day: Day) -> Unit,
    onHolidayClick: (day: Holiday) -> Unit,
) {
    val monthNum by viewModel.getMonth()
    val currentYear by viewModel.getYear()

    val pagerState = rememberPagerState(
        initialPage = monthNum,
        initialPageOffsetFraction = 0f,
        pageCount = { MONTH_COUNT }
    )
    val scope = rememberCoroutineScope()
    val filters = viewModel.getActiveFilters()
    val isLowPerformanceDevice = isLowPerformanceDevice()

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
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    )
                },
                onClick = onTabClick
            )
        }

        HorizontalPager(state = pagerState) { page ->
            val pageOffset = pagerState.currentPageOffsetFraction
            if (!isLowPerformanceDevice || needToShowLayout(pageOffset)) {
                HolidayTileMonthLayout(
                    modifier = Modifier.graphicsLayer {
                        if (!isLowPerformanceDevice) {
                            graphicalLayerTransform(this, pageOffset)
                        }
                    },
                    settingsViewModel = settingsViewModel,
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

fun graphicalLayerTransform(scope: GraphicsLayerScope, pageOffset: Float) {
    scope.apply {
        // We animate the alpha, between 0% and 100%
        alpha = lerp(
            start = ScaleFactor(0f, 0f),
            stop = ScaleFactor(1f, 1f),
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        ).scaleX
    }
}
