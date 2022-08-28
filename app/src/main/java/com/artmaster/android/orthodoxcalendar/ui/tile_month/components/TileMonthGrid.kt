package com.artmaster.android.orthodoxcalendar.ui.tile_month.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.MONDAY
import com.artmaster.android.orthodoxcalendar.domain.Holiday.DayOfWeek.SUNDAY
import com.artmaster.android.orthodoxcalendar.domain.Time

@Preview
@Composable
fun ShowMonth() {
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
        dayInWeek = if (dayInWeek == SUNDAY.num) MONDAY.num
        else dayInWeek.inc()
    }
    TileMonthGrid(days = days, dayOfMonth = time.dayOfMonth)
}

@Composable
fun TileMonthGrid(
    days: List<Day>,
    dayOfMonth: Int,
    pageOffset: Float = 0f,
    onClick: (holiday: Holiday) -> Unit = {}
) {
    var focusedDay by remember { mutableStateOf(dayOfMonth) }

    Row(modifier = Modifier
        .fillMaxSize()
        .graphicsLayer { graphicalLayerTransform(this, pageOffset) }) {

/*      val numDays = daysInMonth
        val firstDayInWeek = days.first().dayInWeek
        val maxWeekCount = ceil((firstDayInWeek + numDays) / 7f).toInt() */

        val maxWeekCount = 6

        var daysCount = 0

        for (currentWeekNum in 0..maxWeekCount) {
            key(currentWeekNum) {
                Column(
                    modifier = Modifier.fillMaxWidth((1f / (maxWeekCount - currentWeekNum + 1))),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (index in 1..SUNDAY.num) {
                        if (currentWeekNum == 0) {
                            DayOfWeekName(dayOfWeekNum = index)
                            continue
                        }
                        val curDayNum = ((currentWeekNum - 1) * 7) + index

                        if (daysCount < days.size && days[daysCount].dayInWeek == index) {
                            MonthDay(days[daysCount], focusedDay == curDayNum) {
                                focusedDay = curDayNum
                            }
                            daysCount++
                        } else EmptyDay()
                    }
                }
            }
        }
    }
}

fun graphicalLayerTransform(scope: GraphicsLayerScope, pageOffset: Float) {
    scope.apply {
        // We animate the scaleX + scaleY, between 85% and 100%
        lerp(
            start = 0.85.dp,
            stop = 1.dp,
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        ).also { scale ->
            scaleX = scale.toPx() / 3
            scaleY = scale.toPx() / 3
        }

        // We animate the alpha, between 50% and 100%
        alpha = lerp(
            start = 0.5.dp,
            stop = 1.dp,
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        ).toPx()
    }
}