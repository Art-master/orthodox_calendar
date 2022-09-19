package com.artmaster.android.orthodoxcalendar.ui.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.OldStyleDateText
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.Ornament

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun HolidayPagePreview() {
    val holiday = Holiday(
        title = "Самый большой праздник",
        description = "Очень длинное описание для праздника"
    )

    val data = remember { mutableStateOf<Holiday?>(holiday) }

    HolidayPage(viewModel = null, currentHoliday = data)
}


@Composable
fun HolidayPage(
    viewModel: CalendarViewModel?,
    currentHoliday: MutableState<Holiday?>
) {
    currentHoliday.value?.let { holiday ->

        val scroll = rememberScrollState(0)
        val sheetPeekHeight = remember { mutableStateOf(200.dp) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Green)
                    .fillMaxHeight(0.6f),
                painter = painterResource(id = R.drawable.image1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )

            HolidayPageTitle(holiday = holiday, headerHeight = sheetPeekHeight.value)
            HolidayDescription(holiday = holiday)
        }
    }
}

@Composable
fun HolidayPageTitle(holiday: Holiday, headerHeight: Dp = 80.dp) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val annotatedString = buildAnnotatedString {
            Ornament(
                builder = this,
                text = stringResource(id = R.string.ornament_for_headers_left)
            )
            append(holiday.title)
            Ornament(
                builder = this,
                text = stringResource(id = R.string.ornament_for_headers_right)
            )
        }

        Text(
            color = DefaultTextColor,
            text = annotatedString,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal)),
            textAlign = TextAlign.Center
        )

        OldStyleDateText(day = holiday.day, month = holiday.month)
    }

}

@Composable
fun HolidayDescription(holiday: Holiday) {

    val description = if (holiday.description.isEmpty()) {
        buildAnnotatedString { append(stringResource(id = R.string.no_description)) }
    } else buildAnnotatedString {
        HeadSymbol(
            builder = this,
            text = holiday.description.first()
        )
        append(holiday.description.substring(1))
    }

    Text(
        modifier = Modifier.padding(start = 5.dp, top = 10.dp, end = 5.dp),
        color = DefaultTextColor,
        text = description,
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.cyrillic_old)),
        textAlign = TextAlign.Justify
    )
}

@Composable
fun HeadSymbol(builder: AnnotatedString.Builder, text: Char) {
    builder.apply {
        withStyle(
            style = SpanStyle(
                color = HeadSymbolTextColor,
                fontFamily = FontFamily(Font(R.font.bukvica)),
                fontSize = 40.sp
            )
        ) {
            append(text)
        }
    }
}
