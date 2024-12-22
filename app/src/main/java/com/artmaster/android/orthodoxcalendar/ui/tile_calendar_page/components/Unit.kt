package com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun Int.scaledSp(): TextUnit {
    val value: Int = this
    return with(LocalDensity.current) {
        val fontScale = this.fontScale
        val textSize = value / fontScale
        textSize.sp
    }
}

val Int.scaledSp: TextUnit
    @Composable get() = scaledSp()


@Composable
fun ScreenAspectRatio(): Float {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    // Calculate the aspect ratio
    return remember(screenWidth, screenHeight) {
        if (screenHeight > 0) screenWidth.toFloat() / screenHeight else 0f
    }
}