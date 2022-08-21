package com.artmaster.android.orthodoxcalendar.ui.tile_month.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_COUNT
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.WindowBackground
import com.google.accompanist.pager.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
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

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun HolidayTileLayout(
    viewModel: CalendarViewModel,
    time: Time,
    onClick: (holiday: Holiday) -> Unit = {}
) {
    var monthNum by remember { mutableStateOf(time.month) }

    val pagerState = rememberPagerState(monthNum)
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.setMonth(page.inc())
            monthNum = page.inc()

            //current page
            viewModel.loadMonthData(monthNum, time.year)
            //next page data
            viewModel.loadMonthData(monthNum + 1, time.year)
            //previous page data
            viewModel.loadMonthData(monthNum - 1, time.year)
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
                count = MONTH_COUNT, state = pagerState, key = { r -> r }
            ) { page ->
                val pageNumStartsWithOne = page.inc()
                val days = viewModel.getCurrentMonthData(monthNum = pageNumStartsWithOne)
                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                HolidayTileMonthLayout(
                    data = days,
                    time = time,
                    pageOffset = pageOffset,
                    isCurrentPage = pageNumStartsWithOne == monthNum
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun ShowTabs() {
    val pagerState = rememberPagerState(1)
    MonthTabs(pagerState = pagerState)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MonthTabs(pagerState: PagerState, onClick: (month: Int) -> Unit = {}) {
    val items = stringArrayResource(id = R.array.months_names_gen)
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = WindowBackground,
        contentColor = Color.Gray,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        items.forEachIndexed { index, title ->
            Tab(
                text = { MonthName(title = title) },
                selected = pagerState.currentPage == index,
                onClick = { onClick.invoke(index.inc()) },
            )
        }
    }
}

@Composable
fun MonthName(title: String) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = HeadSymbolTextColor)) {
            append(title.first())
        }
        append(title.substring(1))
    }

    Text(
        text = annotatedString, color = DefaultTextColor,
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal))
    )
}