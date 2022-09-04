package com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.Pager
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.artmaster.android.orthodoxcalendar.domain.Day

@Preview
@Composable
fun HolidayListPreview() {

}

@Composable
fun HolidayList(data: Pager<Int, Day>) {
    val lazyPagingItems = data.flow.collectAsLazyPagingItems()

    LazyColumn {
        items(lazyPagingItems) { message ->
            if (message != null) {
                HolidayDayItem(message)
            } else {
                //MessagePlaceholder()
                //HolidayDayItem(Day())
            }
        }
    }
}