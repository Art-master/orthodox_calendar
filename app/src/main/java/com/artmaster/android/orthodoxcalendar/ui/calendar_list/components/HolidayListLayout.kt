package com.artmaster.android.orthodoxcalendar.ui.calendar_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
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
fun HolidayPagerListLayout(viewModel: CalendarViewModel = CalendarViewModel()) {
    var monthNum by remember { viewModel.getMonth() }

    val pagerState = rememberPagerState(monthNum.dec())
    val scope = rememberCoroutineScope()

    val onDayClick = remember {
        { day: Day -> viewModel.setDayOfMonth(day = day.dayOfMonth) }
    }

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.setMonth(page)
            monthNum = page

            val year = viewModel.getYear().value

            //current page
            viewModel.loadYearData(year)
            //next page data
            viewModel.loadYearData(year + 1)
            //previous page data
            viewModel.loadYearData(year - 1)
        }
    }

    Column(Modifier.fillMaxHeight()) {

        YearsTabs(pagerState = pagerState) {
            monthNum = it
            scope.launch {
                pagerState.animateScrollToPage(monthNum)
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
                    data = viewModel.getCurrentYearData(yearNum = viewModel.availableYears.first() + page),
                )
            }
        }
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