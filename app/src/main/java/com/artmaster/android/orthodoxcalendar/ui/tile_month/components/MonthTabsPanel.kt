package com.artmaster.android.orthodoxcalendar.ui.tile_month.components

import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.WindowBackground
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState

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