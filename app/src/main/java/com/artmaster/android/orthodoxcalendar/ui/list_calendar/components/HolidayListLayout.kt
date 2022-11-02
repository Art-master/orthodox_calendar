package com.artmaster.android.orthodoxcalendar.ui.list_calendar.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.filters.Tools
import com.artmaster.android.orthodoxcalendar.ui.theme.NoRippleTheme
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.HolidayList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Preview
@Composable
fun HolidayListLayoutPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
            HolidayPagerListLayout()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HolidayPagerListLayout(
    viewModel: CalendarViewModel = CalendarViewModel(),
    onDayClick: (day: Day) -> Unit = {},
    onHolidayClick: (day: Holiday) -> Unit = {},
) {

    val availableYears = viewModel.availableYears
    val startIndex = viewModel.getYear().value - availableYears.first()
    var yearNum by remember { mutableStateOf(startIndex) }

    val pagerState = rememberPagerState(yearNum)
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.setMonth(page)
            yearNum = page

            val year = availableYears.first() + page

            //current page
            viewModel.loadAllHolidaysOfYear(year)
            //next page data
            viewModel.loadAllHolidaysOfYear(year + 1)
            //previous page data
            viewModel.loadAllHolidaysOfYear(year - 1)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxHeight()) {

            YearsTabs(pagerState = pagerState) {
                yearNum = it
                scope.launch {
                    pagerState.animateScrollToPage(yearNum)
                }
            }

            HorizontalPager(
                count = Constants.HolidayList.PAGE_SIZE.value,
                state = pagerState,
                key = { r -> r }
            ) { page ->
                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                if (needToShowLayout(pageOffset)) {
                    HolidayList(
                        modifier = Modifier
                            .graphicsLayer { graphicalLayerTransform(this, pageOffset) },
                        data = viewModel.getCurrentYearData(yearNum = availableYears.first() + page),
                        onDayClick = onDayClick,
                        onHolidayClick = onHolidayClick
                    )
                }
            }
        }
        Tools(parent = this)
    }

}

fun needToShowLayout(pageOffset: Float) = pageOffset < 0.6f

fun graphicalLayerTransform(scope: GraphicsLayerScope, pageOffset: Float) {
    scope.apply {
        // We animate the scaleX + scaleY, between 85% and 100%
        lerp(
            start = 0.8.dp,
            stop = 1.dp,
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        ).also { scale ->
            scaleX = scale.toPx() / 3
            scaleY = scale.toPx() / 3
        }

        // We animate the alpha, between 0% and 100%
        alpha = lerp(
            start = ScaleFactor(0f, 0f),
            stop = ScaleFactor(1f, 1f),
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        ).scaleX
    }
}