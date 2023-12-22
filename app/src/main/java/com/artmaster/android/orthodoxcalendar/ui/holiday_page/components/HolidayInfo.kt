package com.artmaster.android.orthodoxcalendar.ui.holiday_page.components

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.OrtUtils.convertSpToPixels
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.alerts.DeleteHolidayDialog
import com.artmaster.android.orthodoxcalendar.ui.common.Divider
import com.artmaster.android.orthodoxcalendar.ui.theme.*
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.StyleDatesText
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModelFake
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ICalendarViewModel
import java.util.*

@Preview(showBackground = true, device = Devices.PIXEL_3, heightDp = 700)
@Composable
fun HolidayPagePreview() {
    val holiday = Holiday(
        imageId = "image1",
        isCreatedByUser = false,
        title = "Перенесение из Мальты в Гатчину креста из части Древа Животворящего Креста Господня, Филермской иконы Божией Матери и правой руки мощей святого Иоанна Крестителя",
        description = "Много текста описание для праздника. Очень длинное описание для праздника. Очень длинное описание для праздника.Очень длинное описание для праздника. Очень длинное описание для праздника. Очень длинное описание для праздника. Очень длинное описание для праздника"
    )

    HolidayPage(
        viewModel = null,
        holiday = holiday,
        titleHeightInitSize = 800,
        onEditClick = {},
        onDeleteClick = {})
}

@Preview(showBackground = true, device = Devices.PIXEL_3, heightDp = 700)
@Composable
fun UserHolidayPagePreview() {
    val holiday = Holiday(
        imageId = "",
        isCreatedByUser = true,
        title = "Иван Иванович Иванов",
        typeId = Holiday.Type.USERS_BIRTHDAY.id,
        day = 13,
        month = Holiday.Month.DECEMBER.num.inc(),
        year = 1967,
        description = ""
    )

    HolidayPage(
        viewModel = CalendarViewModelFake(),
        holiday = holiday,
        titleHeightInitSize = 800,
        onEditClick = {},
        onDeleteClick = {})
}


@SuppressLint("DiscouragedApi")
@Composable
fun HolidayPage(
    modifier: Modifier = Modifier,
    viewModel: ICalendarViewModel?,
    holiday: Holiday,
    onEditClick: (holiday: Holiday) -> Unit,
    onDeleteClick: (holiday: Holiday) -> Unit,
    isHeaderEnable: Boolean = true,
    titleHeightInitSize: Int = 0 //initial state for preview
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val drawableId = remember(holiday.id) {
        val imageId = holiday.imageId
        if (imageId.isEmpty()) return@remember 0
        context.resources.getIdentifier(imageId, "drawable", context.packageName)
    }

    val scroll = rememberScrollState(0)
    val modalState = remember { mutableStateOf(false) }
    var titleHeight by rememberSaveable { mutableIntStateOf(titleHeightInitSize) }
    val titlePadding = with(LocalDensity.current) {
        val value = configuration.screenHeightDp.dp - titleHeight.toDp()
        if (value.value > 0) value else 10.dp
    }

    val imageHeight = (configuration.screenHeightDp / 1.2).dp

    val onRejectClickRemembered by rememberUpdatedState { modalState.value = false }
    val onConfirmClickRemembered by rememberUpdatedState {
        onDeleteClick(holiday)
        modalState.value = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scroll),
    ) {
        if (drawableId != 0) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background)
                    .height(imageHeight),
                painter = painterResource(id = drawableId),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                alignment = Alignment.Center
            )
        } else {
            NoImageLayout(imageHeight)
        }

        if (holiday.isCreatedByUser) {
            UserHolidayControlMenu(
                modifier = Modifier
                    .padding(top = titlePadding)
                    .offset(y = (-35).dp)
                    .zIndex(10f),
                holiday = holiday,
                onEditClick = onEditClick,
                onDeleteClick = { modalState.value = true }
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = titlePadding)// height control
        ) {
            Column(
                modifier = Modifier
                    .drawBehind {
                        // Shadow
                        if (isHeaderEnable) {
                            drawRoundRect(
                                color = Color.Black,
                                topLeft = Offset(0f, -10f),
                                alpha = 0.3f,
                                cornerRadius = CornerRadius(40f, 40f)
                            )
                        }
                    }
                    .clip(RoundedCornerShape(20.dp))
                    .background(Background)
            ) {
                if (isHeaderEnable) {
                    HolidayPageTitle(
                        modifier = Modifier.onSizeChanged { titleHeight = it.height },
                        holiday = holiday,
                        currentYear = viewModel?.getYear()?.value ?: Time().year
                    )
                }

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
                        fullHolidayInfo = viewModel?.getFullHolidayInfo(holiday.id, holiday.year)
                    }

                    // if full data loaded (etc. include holiday description)
                    fullHolidayInfo?.let {
                        val description = remember {
                            fullHolidayInfo!!.description.substringBefore("<<").trim()
                        }

                        val link = remember {
                            fullHolidayInfo!!.description
                                .substringAfter("<<")
                                .substringBefore(">>")
                        }

                        Divider()
                        HolidayDescriptionLayout(description = description)
                        if (link.isNotBlank()) {
                            SourceLink(link)
                        }
                    }
                }
            }

            DeleteHolidayDialog(
                state = modalState.value,
                onRejectClick = onRejectClickRemembered,
                onConfirmClick = onConfirmClickRemembered
            )
        }
    }
}

@Composable
fun HolidayPageTitle(modifier: Modifier = Modifier, holiday: Holiday, currentYear: Int) {
    val userHolidayNames = stringArrayResource(id = R.array.user_holidays_names)
    Column(
        modifier = modifier
            .fillMaxWidth()
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
                modifier = Modifier.fillMaxWidth(0.8f),
                color = DefaultTextColor,
                text = buildHolidayTitle(holiday, userHolidayNames),
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

        if (holiday.isCreatedByUser) {
            UserHolidayDatesText(holiday = holiday, currentYear = currentYear)
        } else {
            StyleDatesText(day = holiday.day, month = holiday.getMonthWith0(), year = holiday.year)
        }
    }
}

fun buildHolidayTitle(holiday: Holiday, userHolidayNames: Array<String>): String {
    return if (holiday.isCreatedByUser) {
        val resId = when (holiday.typeId) {
            Holiday.Type.USERS_BIRTHDAY.id -> Holiday.Type.USERS_BIRTHDAY.resId
            Holiday.Type.USERS_NAME_DAY.id -> Holiday.Type.USERS_NAME_DAY.resId
            Holiday.Type.USERS_MEMORY_DAY.id -> Holiday.Type.USERS_MEMORY_DAY.resId
            else -> throw IllegalStateException()
        }
        userHolidayNames[resId] + "\n \n" + holiday.title
    } else holiday.title
}

@Composable
fun UserHolidayDatesText(holiday: Holiday, currentYear: Int) {
    val calendar = GregorianCalendar()
    calendar.set(holiday.year, holiday.getMonthWith0(), holiday.day)

    val monthName = stringArrayResource(id = R.array.months_names_acc)[calendar.get(Calendar.MONTH)]
    var newDate = "${calendar.get(Calendar.DAY_OF_MONTH)} " + monthName.lowercase()
    if (holiday.year > 0) newDate += " ${holiday.year} ${stringResource(id = R.string.year_title_short)}"

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = HeadSymbolTextColor)) {
            append(newDate)
        }
        if (holiday.typeId == Holiday.Type.USERS_BIRTHDAY.id && holiday.year > 0) {
            val yearsNum = currentYear - holiday.year
            if (yearsNum > 0) {
                append("\n (")
                withStyle(style = SpanStyle(color = OldDateTextColor)) {
                    append("${stringResource(R.string.age_title)}: $yearsNum")
                }
                append(") ")
            }
        }
    }

    Text(
        color = DefaultTextColor,
        text = annotatedString,
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.cyrillic_old)),
        textAlign = TextAlign.Center
    )
}

val PADDING_BOTTOM = 40.dp

@Composable
fun HolidayDescriptionLayout(description: String) {
    if (description.isEmpty()) {
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
            setText(description.substring(startIndex = 1))
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
            setHeadSymbol(description.first())
        }
    }

    val height = justifiedTextViewCompose.getCalculatedHeight()
    val dpHeight = with(LocalDensity.current) { height.toDp() + PADDING_BOTTOM }
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

@Composable
fun NoImageLayout(imageHeight: Dp) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        NoImageLayout,
                        NoImageLayoutSecondary
                    )
                )
            )
            .height(imageHeight),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.size(60.dp),
            painter = painterResource(id = R.drawable.ic_no_image),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center
        )
        Text(
            textAlign = TextAlign.Center,
            style = TextStyle(color = Color.Transparent),
            text = stringResource(id = R.string.no_image),
            color = NoImageLayoutText,
            fontSize = 25.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old))
        )
    }
}

@Composable
fun SourceLink(link: String) {

    val uriHandler = LocalUriHandler.current
    val onLinkClick by rememberUpdatedState {
        uriHandler.openUri(link)
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .clickable(onClick = onLinkClick),
        text = stringResource(id = R.string.source),
        fontSize = with(LocalDensity.current) {
            (20 / fontScale).sp
        },
        color = LinksColor,
        fontFamily = FontFamily(Font(R.font.decorated)),
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )
}
