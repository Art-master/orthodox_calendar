package com.artmaster.android.orthodoxcalendar.ui.list_calendar_page.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.common.getYears
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.TabsBackground
import com.artmaster.android.orthodoxcalendar.ui.theme.TabsRowContentColor

@Preview(showBackground = true)
@Composable
fun ShowTabs() {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { Constants.HolidayList.PAGE_SIZE.value }
    )

    YearsTabs(pagerState = pagerState)
}

@Composable
fun YearsTabs(pagerState: PagerState, onClick: ((yearIndex: Int) -> Unit)? = null) {
    val items by remember {
        mutableStateOf(getYears(Time().year))
    }

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = TabsBackground,
        contentColor = TabsRowContentColor,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
            )
        }
    ) {
        items.forEachIndexed { index, title ->
            key(title) {
                val onItemClickRemembered by rememberUpdatedState { onClick?.invoke(index) ?: Unit }
                Tab(
                    modifier = Modifier.height(40.dp),
                    text = { YearName(title = title) },
                    selected = pagerState.currentPage == index,
                    onClick = onItemClickRemembered,
                )
            }
        }
    }
}

@Composable
fun YearName(title: String) {
    Text(
        text = title, color = DefaultTextColor,
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal))
    )
}