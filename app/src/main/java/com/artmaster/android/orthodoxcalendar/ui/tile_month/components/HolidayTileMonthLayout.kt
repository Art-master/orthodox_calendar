package com.artmaster.android.orthodoxcalendar.ui.tile_month.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.domain.model.CurrentTime

@Preview
@Composable
fun Preview() {
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

    val monthData = MutableLiveData<List<Day>>()
    val currentTime = CurrentTime(year = time.year, month = time.month, time.dayOfMonth)
    HolidayTileMonthLayout(data = monthData, isCurrentPage = true, time = currentTime)
}

@Composable
fun HolidayTileMonthLayout(
    data: MutableLiveData<List<Day>>,
    time: CurrentTime,
    isCurrentPage: Boolean,
    pageOffset: Float = 0f,
    onClick: (holiday: Holiday) -> Unit = {}
) {
    val days = data.observeAsState(emptyList())
    Column {
        if (days.value.isEmpty() || isCurrentPage.not()) {
            Spinner()
        } else {
            TileMonthGrid(days.value, time.dayOfMonth, pageOffset, onClick)
        }
        //HolidayList(data = Pager<Int, Day>())
    }
}