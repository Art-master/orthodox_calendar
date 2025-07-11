package com.artmaster.android.orthodoxcalendar.ui.common

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.asComposeRenderEffect

/**
 * https://www.sinasamaki.com/pager-animations/
 */
fun graphicalLayerTransform(
    scope: GraphicsLayerScope,
    pageOffset: Float,
    state: PagerState
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

        scope.apply {
            val startOffset = state.currentPageOffsetFraction
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
}