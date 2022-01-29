package com.artmaster.android.orthodoxcalendar.ui.tile_month.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.theme.*

@Preview
@Composable
fun PreviewGrid() {
    MonthDay(
        Day(
            year = 2022,
            month = 1,
            dayOfMonth = 1,
            dayInWeek = 5,
            holidays = arrayListOf(
                Holiday(year = 2022, month = 1, day = 1, typeId = Holiday.Type.MAIN.id)
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
}

@Composable
fun MonthDay(day: Day) {
    ApplicationsTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Box(
                Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .background(getTypeHolidayColor(day), shape = RoundedCornerShape(13.dp))
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(13.dp)
                    )
            ) {
                Text(
                    modifier = Modifier.fillMaxSize(0.7f),
                    text = day.dayOfMonth.toString(),
                    fontSize = 42.sp,
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .fillMaxHeight()
                        .align(Alignment.BottomEnd)
                ) {
                    if (day.holidays.isNotEmpty()) {
                        HolidaysDot()
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                        .align(Alignment.BottomStart)
                ) {
                    HolidayPermissions(day = day)
                }
            }
        }
    }
}

private fun getTypeHolidayColor(day: Day): Color {
    val holidays = day.holidays

    return when {
        isTypeHoliday(Holiday.Type.TWELVE_MOVABLE, holidays) ||
                isTypeHoliday(Holiday.Type.TWELVE_NOT_MOVABLE, holidays) -> {
            TwelveHoliday
        }
        isTypeHoliday(Holiday.Type.GREAT_NOT_TWELVE, holidays) -> HeadHoliday
        isTypeHoliday(Holiday.Type.MAIN, holidays) -> Easter
        day.fasting.type == Fasting.Type.FASTING -> FastingDay
        day.fasting.type == Fasting.Type.FASTING_DAY -> FastingDay
        day.fasting.type == Fasting.Type.SOLID_WEEK -> DayOfSolidWeek
        else -> UsualDay
    }
}

private fun isTypeHoliday(type: Holiday.Type, holidays: List<Holiday>): Boolean {
    for (holiday in holidays) {
        if (holiday.typeId == type.id) return true
    }
    return false
}

@Composable
private fun HolidayPermissions(day: Day) {
    if (day.fasting.type == Fasting.Type.NONE) return
    for (permission in day.fasting.permissions) {
        when (permission) {
            Fasting.Permission.OIL -> PermissionImage(resId = R.drawable.sun)
            Fasting.Permission.FISH -> PermissionImage(resId = R.drawable.fish)
            Fasting.Permission.VINE -> PermissionImage(resId = R.drawable.vine)
            Fasting.Permission.STRICT -> PermissionImage(resId = R.drawable.fish)
            Fasting.Permission.NO_EAT -> {}
            Fasting.Permission.CAVIAR -> {}
            Fasting.Permission.HOT_NO_OIL -> {}
            Fasting.Permission.NO_MEAT -> PermissionImage(resId = R.drawable.eggs)
        }
    }
}

@Composable
fun PermissionImage(resId: Int) {
    Image(
        modifier = Modifier
            .rotate(0f)
            .fillMaxHeight(),
        painter = painterResource(id = resId),
        contentDescription = ""
    )
}

@Composable
fun HolidaysDot() {
    Image(
        modifier = Modifier
            .rotate(0f)
            .fillMaxHeight(),
        painter = painterResource(id = R.drawable.dot),
        contentDescription = ""
    )
}