package com.artmaster.android.orthodoxcalendar.ui.tile_month.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.theme.*

@Preview
@Composable
fun PreviewGrid() {
    val time = Time()
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
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
            ), isActive = false
        )
    }
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
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(getTypeHolidayColor(day), shape = RoundedCornerShape(13.dp))
            .border(
                width = 1.dp,
                color = if (isActive) Color.Red else Color.Black,
                shape = RoundedCornerShape(13.dp)
            )
            .clickable {
                onClick.invoke()
            }
    ) {
        ConstraintLayout {
            Text(
                modifier = Modifier.fillMaxSize(0.7f),
                text = day.dayOfMonth.toString(),
                fontSize = 30.sp,
                color = DefaultTextColor,
                fontFamily = FontFamily(Font(R.font.ort_basic, FontWeight.Normal)),
                textAlign = TextAlign.Left
            )
            //if (day.holidays.isNotEmpty()) HolidaysDot()
            HolidaysDot()
            HolidaysDot()
            HolidaysDot()
            HolidaysDot()
            HolidaysDot()
            //HolidayPermissions(day = day)
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
            Fasting.Permission.OIL -> PermissionImage(resId = R.drawable.ic_tile_sun)
            Fasting.Permission.FISH -> PermissionImage(resId = R.drawable.ic_tile_fish)
            Fasting.Permission.VINE -> PermissionImage(resId = R.drawable.ic_tile_vine)
            Fasting.Permission.STRICT -> PermissionImage(resId = R.drawable.ic_tile_triangle)
            Fasting.Permission.NO_EAT -> {}
            Fasting.Permission.CAVIAR -> {}
            Fasting.Permission.HOT_NO_OIL -> {}
            Fasting.Permission.NO_MEAT -> PermissionImage(resId = R.drawable.ic_tile_eggs)
        }
    }
}

@Composable
fun PermissionImage(resId: Int) {
    Image(
        modifier = Modifier
            .rotate(0f)
            .padding(start = 2.dp)
            .fillMaxHeight(),
        painter = painterResource(id = resId),
        contentDescription = ""
    )
}

@Composable
fun HolidaysDot() {
    val backgroundColor = listOf(Color(0xFF2078EE), Color(0xFF74E6FE))
    val sunColor = listOf(Color(0xFFFFC200), Color(0xFFFFE100))
    Canvas(
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp)
    ) {
        val width = size.width
        val height = size.height
        val path = Path().apply {
            moveTo(width.times(.76f), height.times(.72f))
            cubicTo(
                width.times(.93f),
                height.times(.72f),
                width.times(.98f),
                height.times(.41f),
                width.times(.76f),
                height.times(.40f)
            )
            cubicTo(
                width.times(.75f),
                height.times(.21f),
                width.times(.35f),
                height.times(.21f),
                width.times(.38f),
                height.times(.50f)
            )
            cubicTo(
                width.times(.25f),
                height.times(.50f),
                width.times(.20f),
                height.times(.69f),
                width.times(.41f),
                height.times(.72f)
            )
            close()
        }
        drawRoundRect(
            brush = Brush.verticalGradient(backgroundColor),
            cornerRadius = CornerRadius(50f, 50f),

            )
        drawCircle(
            brush = Brush.verticalGradient(sunColor),
            radius = width.times(.17f),
            center = Offset(width.times(.35f), height.times(.35f))
        )
        drawPath(path = path, color = Color.White.copy(alpha = .90f))
    }
}