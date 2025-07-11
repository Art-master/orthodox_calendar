package com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_COUNT
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.TabsBackground
import com.artmaster.android.orthodoxcalendar.ui.theme.TabsRowContentColor
import com.artmaster.android.orthodoxcalendar.ui.theme.holidayMonthTabsHeight

@Preview(showBackground = true)
@Composable
fun ShowTabs() {
    val pagerState = rememberPagerState(
        initialPage = 1,
        initialPageOffsetFraction = 0f,
        pageCount = { MONTH_COUNT }
    )
    MonthTabs(
        currentPage = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
            )
        })
}

@Composable
fun MonthTabs(
    currentPage: Int,
    indicator: @Composable @UiComposable (tabPositions: List<TabPosition>) -> Unit,
    onClick: ((month: Int) -> Unit)? = null
) {
    val items = stringArrayResource(id = R.array.months_names_gen)
    val monthNames = remember { items }

    ScrollableTabRow(
        selectedTabIndex = currentPage,
        backgroundColor = TabsBackground,
        contentColor = TabsRowContentColor,
        indicator = indicator
    ) {
        monthNames.forEachIndexed { index, title ->
            key(title) {
                val onItemClickRemembered by rememberUpdatedState { onClick?.invoke(index) ?: Unit }
                Tab(
                    modifier = Modifier.height(holidayMonthTabsHeight),
                    text = { MonthName(title = title) },
                    selected = currentPage == index,
                    onClick = onItemClickRemembered,
                )
            }
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