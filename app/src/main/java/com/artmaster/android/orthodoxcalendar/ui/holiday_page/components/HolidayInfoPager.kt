package com.artmaster.android.orthodoxcalendar.ui.holiday_page.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.Spinner
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

@Preview(showBackground = true, device = Devices.PIXEL_3, heightDp = 700)
@Composable
fun HolidayInfoPagerPreview() {
    HolidayInfoPager(viewModel = null, holidayId = 0L)
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun HolidayInfoPager(
    viewModel: CalendarViewModel?,
    holidayId: Long,
    onEditClick: (holiday: Holiday) -> Unit = {},
    onDeleteClick: (holiday: Holiday) -> Unit = {}
) {
    viewModel ?: return

    var isInit by remember { mutableStateOf(false) }

    val year = viewModel.getYear().value
    var currentHolidayIndex by remember { mutableStateOf(0) }
    var holidays by remember { mutableStateOf(emptyList<Holiday>()) }


    LaunchedEffect(year) {
        viewModel.loadAllHolidaysOfYear(year)
        val hs = viewModel.getAllHolidaysOfYear()
        currentHolidayIndex = hs.indexOfFirst { h -> h.id == holidayId }
        holidays = hs
        isInit = true
    }

    if (isInit.not()) {
        Spinner()
        return
    }

    val pagerState = rememberPagerState(currentHolidayIndex)

    HorizontalPager(
        count = holidays.size,
        state = pagerState,
        key = { r -> r }
    ) { page ->
        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

        if (needToShowLayout(pageOffset)) {
            HolidayPage(
                modifier = Modifier.graphicsLayer { graphicalLayerTransform(this, pageOffset) },
                viewModel = viewModel,
                holiday = holidays[page],
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

fun needToShowLayout(pageOffset: Float) = pageOffset < 0.8f

fun graphicalLayerTransform(scope: GraphicsLayerScope, pageOffset: Float) {
    scope.apply {
        // We animate the scaleX + scaleY, between 85% and 100%
        lerp(
            start = 0.8.dp,
            stop = 1.dp,
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        ).also { scale ->
            scaleX = scale.toPx() / 3
            scaleY = scale.toPx() / 3
        }

        // We animate the alpha, between 0% and 100%
        alpha = lerp(
            start = ScaleFactor(0f, 0f),
            stop = ScaleFactor(1f, 1f),
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        ).scaleX
    }
}
