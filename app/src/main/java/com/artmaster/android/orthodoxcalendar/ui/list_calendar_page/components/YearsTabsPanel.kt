package com.artmaster.android.orthodoxcalendar.ui.list_calendar_page.components

import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.common.getYears
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.TabsBackground
import com.artmaster.android.orthodoxcalendar.ui.theme.TabsRowContentColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun ShowTabs() {
    val pagerState = rememberPagerState(1)
    YearsTabs(pagerState = pagerState)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun YearsTabs(pagerState: PagerState, onClick: (yearIndex: Int) -> Unit = {}) {
    val items by remember {
        mutableStateOf(getYears(Time().year))
    }

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = TabsBackground,
        contentColor = TabsRowContentColor,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        items.forEachIndexed { index, title ->
            key(title) {
                Tab(
                    text = { YearName(title = title) },
                    selected = pagerState.currentPage == index,
                    onClick = { onClick(index) },
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