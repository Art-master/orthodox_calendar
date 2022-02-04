package com.artmaster.android.orthodoxcalendar.ui.tile_month.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.Easter

@Preview
@Composable
fun HolidayListItemPreview() {
    val time = Time()
    HolidayDayItem(
        day = Day(
            year = time.year,
            month = time.month,
            dayOfMonth = time.dayOfMonth,
            dayInWeek = time.dayOfWeek,
            holidays = arrayListOf(
                Holiday(
                    title = "Праздник",
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
        )
    )
}

@Composable
fun HolidayDayItem(day: Day, onClick: (holiday: Holiday) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(20.dp, 100.dp)
    ) {

        ItemHeader(day = day)

        day.holidays.forEach {
//      AnimatedVisibility(visible = true)
            HolidayItem(holiday = it)
        }
    }
}

@Composable
fun ItemHeader(day: Day, onClick: (holiday: Holiday) -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            color = Color.Red,
            text = stringResource(id = R.string.ornament_for_headers_left),
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.ornament, FontWeight.Normal)),
            textAlign = TextAlign.Right
        )
        Text(
            color = DefaultTextColor,
            text = stringArrayResource(id = R.array.daysNames)[day.dayInWeek - 1],
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal)),
            textAlign = TextAlign.Center
        )
        Text(
            color = Color.Red,
            text = stringResource(id = R.string.ornament_for_headers_right),
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.ornament, FontWeight.Normal)),
            textAlign = TextAlign.Left
        )
    }
}

@Composable
fun HolidayItem(holiday: Holiday) {
    val params = getTypyconFontImageAndColor(holiday = holiday)
    Row {
        Text(
            modifier = Modifier.fillMaxWidth(0.1f),
            color = params.second,
            text = stringResource(id = params.first),
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.ort_basic, FontWeight.Normal)),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(0.9f),
            color = DefaultTextColor,
            text = holiday.title,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal)),
            textAlign = TextAlign.Left
        )
    }
}


fun getTypyconFontImageAndColor(holiday: Holiday): Pair<Int, Color> {
    return when (holiday.typeId) {
        Holiday.Type.MAIN.id -> R.string.main_holiday to Color.Blue
        Holiday.Type.TWELVE_MOVABLE.id,
        Holiday.Type.TWELVE_NOT_MOVABLE.id,
        Holiday.Type.GREAT_NOT_TWELVE.id -> R.string.head_holiday to Color.Red
        Holiday.Type.AVERAGE_POLYLEIC.id -> R.string.average_polyleic_holiday to Color.Red
        Holiday.Type.AVERAGE_PEPPY.id -> R.string.average_peppy_holiday to Color.Red
        Holiday.Type.COMMON_MEMORY_DAY.id,
        Holiday.Type.USERS_MEMORY_DAY.id -> R.string.memorial_day to Color.Black
        Holiday.Type.USERS_BIRTHDAY.id -> R.string.birthday to Easter
        Holiday.Type.USERS_NAME_DAY.id -> R.string.name_day to Easter
        else -> 0 to Color.Black
    }
}