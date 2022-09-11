package com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
fun ItemListPreview() {
    val time = Time()
    val dayInWeek = 3

    val day = Day(
        year = time.year,
        month = time.month,
        dayOfMonth = time.dayOfMonth,
        dayInWeek = dayInWeek,
        holidays = arrayListOf(),
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
    //HolidayItem(day = day)
}

@Composable
fun HolidayItem(holiday: Holiday, onClick: (holiday: Holiday) -> Unit = {}) {
    val params = getTypyconFontImageAndColor(holiday = holiday)
    Row(modifier = Modifier.clickable {
        onClick.invoke(holiday)
    }) {
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