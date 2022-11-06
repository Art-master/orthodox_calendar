package com.artmaster.android.orthodoxcalendar.ui.review.components

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.OrtUtils.convertSpToPixels
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.common.Divider
import com.artmaster.android.orthodoxcalendar.ui.theme.Background
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.StyleDatesText

@Preview(showBackground = true, device = Devices.PIXEL_3, heightDp = 700)
@Composable
fun HolidayPagePreview() {
    val holiday = Holiday(
        imageId = "image1",
        title = "Святителей Московских и всея России чудотворцев Петра, Алексия, Ионы, Макария, Филиппа, Иова, Ермогена, Филарета, Иннокентия, Тихона, Макария и Петра",
        description = "Много текста описание для праздника. Очень длинное описание для праздника. Очень длинное описание для праздника.Очень длинное описание для праздника. Очень длинное описание для праздника. Очень длинное описание для праздника. Очень длинное описание для праздника"
    )

    HolidayPage(viewModel = null, holiday = holiday)
}


@SuppressLint("DiscouragedApi")
@Composable
fun HolidayPage(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel?,
    holiday: Holiday
) {

    val context = LocalContext.current
    val drawableId = remember {
        val imageId = holiday.imageId
        if (imageId.isEmpty()) {
            return@remember R.drawable.image_holiday
        }
        val id = context.resources.getIdentifier(imageId, "drawable", context.packageName)
        if (id == 0) { // if not found
            R.drawable.image_holiday
        } else id
    }

    val scroll = rememberScrollState(0)

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scroll),
    ) {
        val configuration = LocalConfiguration.current
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .background(Background)
                .height((configuration.screenHeightDp / 1.2).dp),
            painter = painterResource(id = drawableId),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            alignment = Alignment.Center
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = (configuration.screenHeightDp / 1.5).dp) // height control
                .drawBehind {
                    // Shadow
                    drawRoundRect(
                        color = Color.Black,
                        topLeft = Offset(0f, -10f),
                        alpha = 0.3f,
                        cornerRadius = CornerRadius(40f, 40f)
                    )
                }
                .clip(RoundedCornerShape(20.dp))
                .background(Background)
        ) {

            HolidayPageTitle(holiday = holiday)

            var scrollActivate by remember { mutableStateOf(false) }
            var fullHolidayInfo by remember { mutableStateOf<Holiday?>(null) }

            if (scroll.isScrollInProgress && scrollActivate.not()) {
                scrollActivate = true
            }

            if (fullHolidayInfo == null) {
                // Empty space for starting scrolling
                Spacer(modifier = Modifier.height(300.0.dp))
            }

            // Lazy loading when user scrolling page
            if (scrollActivate) {

                LaunchedEffect(holiday) {
                    val data = viewModel?.getFullHolidayInfo(holiday.id, holiday.year)
                    fullHolidayInfo = data
                }

                // if full data loaded (etc. include holiday description)
                fullHolidayInfo?.let {
                    Divider()
                    HolidayDescriptionLayout(holiday = it)
                }
            }
        }
    }
}

@Composable
fun HolidayPageTitle(holiday: Holiday) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(top = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 2.dp),
                color = HeadSymbolTextColor,
                text = stringResource(id = R.string.ornament_for_headers_left),
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.ornament)),
                textAlign = TextAlign.Right
            )
            Text(
                modifier = Modifier.fillMaxWidth(0.9f),
                color = DefaultTextColor,
                text = holiday.title,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal)),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(end = 2.dp),
                color = HeadSymbolTextColor,
                text = stringResource(id = R.string.ornament_for_headers_right),
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.ornament)),
                textAlign = TextAlign.Left
            )
        }

        StyleDatesText(day = holiday.day, month = holiday.getMonthWith0())
    }

}

@Composable
fun HolidayDescriptionLayout(holiday: Holiday) {
    if (holiday.description.isEmpty()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            color = DefaultTextColor,
            text = stringResource(id = R.string.no_description),
            fontSize = 23.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal)),
            textAlign = TextAlign.Center
        )
        return
    }

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
            setLeadingMargin(3, 9)
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
