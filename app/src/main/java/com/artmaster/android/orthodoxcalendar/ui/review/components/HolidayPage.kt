package com.artmaster.android.orthodoxcalendar.ui.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.OldStyleDateText
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.Ornament

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun HolidayPagePreview() {
    val holiday = Holiday(title = "Самый большой праздник")

    val data = remember { mutableStateOf<Holiday?>(holiday) }

    HolidayPage(viewModel = null, currentHoliday = data)
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HolidayPage(
    viewModel: CalendarViewModel?,
    currentHoliday: MutableState<Holiday?>
) {
    currentHoliday.value?.let { holiday ->

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp
        val density = LocalDensity.current

        val sheetPeekHeight = remember { mutableStateOf(200.dp) }

        val state = rememberDrawerState(DrawerValue.Closed)
        val drawerState = rememberBottomSheetScaffoldState(state)

        BottomSheetScaffold(
            sheetContent = {
                HolidayPageTitle(holiday, sheetPeekHeight.value)
            },
            scaffoldState = drawerState,
            sheetShape = RoundedCornerShape(5.dp),
            sheetElevation = 8.dp,
            backgroundColor = Color.Transparent,
            sheetPeekHeight = sheetPeekHeight.value
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Green)
                    .fillMaxHeight(0.6f)
/*                    .onSizeChanged { size ->
                        with(density) {
                            sheetPeekHeight.value = (screenHeight -size.height).toDp()
                        }
                    }*/
                    .onGloballyPositioned { coords ->
                        with(density) {
                            val bottom = coords.boundsInRoot().bottom.toDp()
                            sheetPeekHeight.value = screenHeight.dp - bottom
                        }

                    },
                painter = painterResource(id = R.drawable.image1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }
    }
}

@Composable
fun HolidayPageTitle(holiday: Holiday, headerHeight: Dp = 80.dp) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 15.dp)
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .height(headerHeight),
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

}