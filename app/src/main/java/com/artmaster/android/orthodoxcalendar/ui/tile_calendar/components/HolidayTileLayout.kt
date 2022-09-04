package com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.lifecycle.MutableLiveData
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_COUNT
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
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
fun HolidayTileLayout(viewModel: CalendarViewModel) {
    var monthNum by remember { viewModel.getMonth() }

    val pagerState = rememberPagerState(monthNum)
    val scope = rememberCoroutineScope()

    val onDayClick = remember {
        { day: Day -> viewModel.setDayOfMonth(day = day.dayOfMonth) }
    }

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.setMonth(page.inc())
            monthNum = page.inc()

            val year = viewModel.getYear().value

            //current page
            viewModel.loadMonthData(monthNum, year)
            //next page data
            viewModel.loadMonthData(monthNum + 1, year)
            //previous page data
            viewModel.loadMonthData(monthNum - 1, year)
        }
    }

    Column(Modifier.fillMaxHeight()) {

        MonthTabs(pagerState = pagerState) {
            monthNum = it
            scope.launch {
                //pagerState.scrollToPage(monthNum.dec())
                pagerState.animateScrollToPage(monthNum.dec())
            }
        }

        Row {
            HorizontalPager(
                modifier = Modifier.padding(15.dp),
                count = MONTH_COUNT,
                state = pagerState,
                key = { r -> r }
            ) { page ->
                val pageNumStartsWithOne = page.inc()
                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                if (needToShowLayout(pageOffset)) {
                    HolidayTileMonthLayout(
                        modifier = Modifier
                            .graphicsLayer { graphicalLayerTransform(this, pageOffset) },
                        data = viewModel.getCurrentMonthData(monthNum = pageNumStartsWithOne),
                        dayOfMonth = viewModel.getDayOfMonth().value,
                        onDayClick = onDayClick
                    )
                }
            }
        }
    }
}

fun needToShowLayout(pageOffset: Float) = pageOffset < 0.7f

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
