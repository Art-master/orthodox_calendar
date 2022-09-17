package com.artmaster.android.orthodoxcalendar.ui.review.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel

@Preview
@Composable
fun HolidayPagePreview() {
    val holiday = Holiday()

    val data = remember { mutableStateOf<Holiday?>(holiday) }

    HolidayPage(currentHoliday = data)
}


@Composable
fun HolidayPage(
    viewModel: CalendarViewModel = CalendarViewModel(),
    currentHoliday: MutableState<Holiday?>
) {

}