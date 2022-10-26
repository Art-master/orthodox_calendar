package com.artmaster.android.orthodoxcalendar.ui.review.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.OrtUtils.convertSpToPixels
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.OldStyleDateText
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.Ornament

@Preview(showBackground = true, device = Devices.PIXEL_3, heightDp = 700)
@Composable
fun HolidayPagePreview() {
    val holiday = Holiday(
        title = "Самый большой праздник",
        description = "Просто длинное описание для праздника. Очень длинное описание для праздника. Очень длинное описание для праздника.Очень длинное описание для праздника. Очень длинное описание для праздника. Очень длинное описание для праздника. Очень длинное описание для праздника"
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
            HolidayDescriptionLayout(holiday = holiday)
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
fun HolidayDescriptionLayout(holiday: Holiday) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidthPx = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }
    var size by remember { mutableStateOf(IntSize.Zero) }

    val justifiedTextViewCompose = remember(size.width) {
        val width = if (size.width > 0) size.width else screenWidthPx.toInt()
        JustifiedTextViewCompose(context).apply {
            setWidth(width)
            setRawTextSize(convertSpToPixels(context, 20f))
            setTextColor(DefaultTextColor)
            setFont(R.font.cyrillic_old)
            setLeadingMargin(3, 6)
            setText(holiday.description.substring(startIndex = 1))
            calculate()
        }
    }

    val symbolSizePx = with(LocalDensity.current) { 60.dp.toPx() }
    val headSymbol = remember(size.width) {
        HeadSymbol(context).apply {
            setSize(symbolSizePx.toInt())
            setRawTextSize(convertSpToPixels(context, 60f))
            setTextColor(HeadSymbolTextColor)
            setFont(R.font.bukvica)
            setTextScaleX(1.5f)
            setHeadSymbol(holiday.description.first())
        }
    }

    val height = justifiedTextViewCompose.getCalculatedHeight()
    val dpHeight = with(LocalDensity.current) { height.toDp() }
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(dpHeight)
        .padding(horizontal = 8.dp)
        .onSizeChanged { size = it },
        onDraw = {
            drawContext.canvas.nativeCanvas.apply {
                headSymbol.onDraw(this)
                justifiedTextViewCompose.onDraw(this)
            }
        })
}
