package com.artmaster.android.orthodoxcalendar.ui.common

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.asComposeRenderEffect
import com.google.accompanist.pager.ExperimentalPagerApi

/**
 * https://www.sinasamaki.com/pager-animations/
 */
@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPagerApi::class)
fun graphicalLayerTransform(
    scope: GraphicsLayerScope,
    pageOffset: Float,
    state: com.google.accompanist.pager.PagerState
) {
    scope.apply {
        val startOffset = state.currentPageOffset
        translationX = size.width * (startOffset * .99f)

        alpha = (2f - startOffset) / 2f
        val blur = (startOffset * 20f).coerceAtLeast(0.1f)
        renderEffect = RenderEffect
            .createBlurEffect(
                blur, blur, Shader.TileMode.DECAL
            ).asComposeRenderEffect()

        val scale = 1f - (startOffset * .1f)
        scaleX = scale
        scaleY = scale
    }
}