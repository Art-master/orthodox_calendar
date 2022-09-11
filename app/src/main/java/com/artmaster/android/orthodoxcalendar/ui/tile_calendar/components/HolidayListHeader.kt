package com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH

@Preview
@Composable
fun ItemHeaderPreview() {
    val time = Time()
    val dayInWeek = 3

    val day = Day(
        year = time.year,
        month = time.month,
        dayOfMonth = time.dayOfMonth,
        dayInWeek = dayInWeek,
        holidays = arrayListOf(),
        fasting = Fasting(
            Fasting.Type.FASTING_DAY,
            permissions = listOf(
                Fasting.Permission.FISH,
                Fasting.Permission.VINE,
                Fasting.Permission.OIL,
                Fasting.Permission.CAVIAR,
            )
        )
    )
    ItemHeader(day = day)
}

@Composable
fun ItemHeader(day: Day, headerHeight: Dp = 300.dp, onClick: (day: Day) -> Unit = {}) {
    val title = day.run {
        "$dayOfMonth ${stringArrayResource(id = R.array.months_names_acc)[day.month]}"
    }

    Column(
        Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .clickable { onClick(day) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val annotatedString = buildAnnotatedString {
            Ornament(builder = this, text = stringResource(id = R.string.ornament_for_headers_left))
            append(title)
            Ornament(
                builder = this,
                text = stringResource(id = R.string.ornament_for_headers_right)
            )
        }

        Text(
            color = DefaultTextColor,
            text = annotatedString,
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal)),
            textAlign = TextAlign.Center
        )

        OldStyleDateText(day = day.dayOfMonth, month = day.month)
        if (day.fasting.type != Fasting.Type.NONE) FastingText(day = day)

        HolidaysDivider()

    }
}

@Composable
fun Ornament(builder: AnnotatedString.Builder, text: String) {
    builder.apply {
        withStyle(
            style = SpanStyle(
                color = HeadSymbolTextColor,
                fontFamily = FontFamily(Font(R.font.ornament)),
                fontSize = 15.sp
            )
        ) {
            append(text)
        }
    }
}

@Composable
fun OldStyleDateText(day: Int, month: Int) {
    val calendar = GregorianCalendar()
    calendar.set(Time().year, month, day)
    calendar.gregorianChange = Date(Long.MAX_VALUE)
    val monthName = stringArrayResource(id = R.array.months_names_acc)[calendar.get(MONTH)]
    val date = "${calendar.get(DAY_OF_MONTH)} " + monthName.lowercase()
    val text = "(" + stringResource(id = R.string.old_style_date_string, date) + ")"

    Text(
        color = DefaultTextColor,
        text = text,
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal)),
        textAlign = TextAlign.Center
    )
}

@Composable
fun FastingText(day: Day) {
    val drawableId = getIdByFasting(day.fasting.type)
    val text = stringResource(id = drawableId)

    Text(
        modifier = Modifier.padding(top = 10.dp),
        color = DefaultTextColor,
        text = text,
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal)),
        textAlign = TextAlign.Center
    )
}

fun getIdByFasting(type: Fasting.Type): Int {
    return when (type) {
        Fasting.Type.NONE -> 0
        Fasting.Type.PETER_AND_PAUL_FASTING -> R.string.peter_and_paul_fasting
        Fasting.Type.ASSUMPTION_FASTING -> R.string.assumption_fasting
        Fasting.Type.CHRISTMAS_FASTING -> R.string.christmas_fasting
        Fasting.Type.GREAT_FASTING -> R.string.great_fasting
        Fasting.Type.FASTING_DAY -> R.string.fasting_day
        Fasting.Type.SOLID_WEEK -> R.string.solid_week
    }
}

@Composable
fun HolidaysDivider() {
    Row(
        modifier = Modifier.padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            color = DefaultTextColor,
            text = stringResource(id = R.string.ornament_for_name_date_left),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ornament)),
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .offset(y = 3.dp)
                .background(DefaultTextColor)
        )
        Text(
            color = DefaultTextColor,
            text = stringResource(id = R.string.ornament_for_name_date_right),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ornament)),
            textAlign = TextAlign.Center
        )
    }
}