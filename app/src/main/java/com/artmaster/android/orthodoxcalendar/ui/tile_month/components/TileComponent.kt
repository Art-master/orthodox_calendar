package com.artmaster.android.orthodoxcalendar.ui.tile_month.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.theme.*

@Composable
fun PreviewGrid(size: Dp = 50.dp) {
    val time = Time()
    Box(
        modifier = Modifier.size(size)
    ) {
        MonthDay(
            Day(
                year = time.year,
                month = time.month,
                dayOfMonth = time.dayOfMonth,
                dayInWeek = time.dayOfWeek,
                holidays = arrayListOf(
                    Holiday(
                        year = time.year,
                        month = time.month,
                        day = time.dayOfMonth,
                        typeId = Holiday.Type.AVERAGE_PEPPY.id
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
            ), isActive = false
        )
    }
}

@Preview(widthDp = 50, heightDp = 50)
@Composable
fun NormalPreview() {
    PreviewGrid()
}

@Preview(widthDp = 150, heightDp = 150)
@Composable
fun MinPreview() {
    PreviewGrid(150.dp)
}

@Composable
fun EmptyDay() {
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(color = Color.Transparent)
    )
}

@Composable
fun DayOfWeekName(dayOfWeekNum: Int) {
    val daysNames = stringArrayResource(id = R.array.daysNamesAbb)
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(color = Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = daysNames[dayOfWeekNum - 1],
            fontSize = 20.sp,
            color = DefaultTextColor,
            fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MonthDay(day: Day, isActive: Boolean, onClick: () -> Unit = {}) {
    val holidayColor = remember {
        getTypeHolidayColor(day)
    }

    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(holidayColor, shape = RoundedCornerShape(6.dp))
            .border(
                width = 1.dp,
                color = if (isActive) Color.Red else Color.Black,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable {
                onClick.invoke()
            }
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize(0.7f)
                .padding(start = 2.dp, top = 2.dp),
            text = day.dayOfMonth.toString(),
            fontSize = 30.sp,
            color = DefaultTextColor,
            fontFamily = FontFamily(Font(R.font.ort_basic, FontWeight.Normal)),
            textAlign = TextAlign.Left
        )
        if (day.holidays.isNotEmpty()) {
            HolidaysIndicator(this, holidayColor)
        }

        HolidayPermissions(this, day = day)
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
private fun HolidayPermissions(parent: BoxScope, day: Day) {
    val padding = 3.dp
    parent.apply {
        if (day.fasting.type == Fasting.Type.NONE) return
        for (permission in day.fasting.permissions) {
            when (permission) {
                Fasting.Permission.OIL -> PermissionImage(
                    resId = R.drawable.ic_tile_sun,
                    Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = padding)
                )
                Fasting.Permission.FISH -> PermissionImage(
                    resId = R.drawable.ic_tile_fish,
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = padding, bottom = padding)
                )
                Fasting.Permission.VINE -> PermissionImage(
                    resId = R.drawable.ic_tile_vine,
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = padding)
                )
                Fasting.Permission.STRICT -> PermissionImage(
                    resId = R.drawable.ic_tile_triangle,
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = padding, bottom = padding)
                )
                Fasting.Permission.NO_EAT -> {}
                Fasting.Permission.CAVIAR -> {}
                Fasting.Permission.HOT_NO_OIL -> {}
                Fasting.Permission.NO_MEAT -> PermissionImage(
                    resId = R.drawable.ic_tile_eggs,
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = padding, bottom = padding)
                )
            }
        }
    }
}

@Composable
fun PermissionImage(resId: Int, modifier: Modifier) {
    Image(
        modifier = Modifier
            .then(modifier)
            .rotate(0f)
            .fillMaxSize(0.25f),
        painter = painterResource(id = resId),
        contentDescription = ""
    )
}

@Composable
fun HolidaysIndicator(parent: BoxScope, holidayColor: Color) {
    parent.apply {
        val isBright = holidayColor == HeadHoliday || holidayColor == Easter

        val strokeColor = if (isBright) {
            listOf(Color(0xFF000000), Color(0xFF000000))
        } else {
            listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF))
        }

        val background = if (isBright) {
            listOf(Color(0xFFFFFFFF), Color(0xFFFFE4E4))
        } else {
            listOf(Color(0xFFFA2222), Color(0xFFFD4747))
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize(0.2f)
                .padding(top = 3.dp, end = 3.dp)
                .align(Alignment.TopEnd)
        ) {
            val width = size.width
            val height = size.height

            drawCircle(
                brush = Brush.verticalGradient(strokeColor),
                radius = width.times(.43f),
                center = Offset(width.times(.50f), height.times(.50f))
            )

            drawCircle(
                brush = Brush.verticalGradient(background),
                radius = width.times(.4f),
                center = Offset(width.times(.50f), height.times(.50f))
            )
        }
    }
}