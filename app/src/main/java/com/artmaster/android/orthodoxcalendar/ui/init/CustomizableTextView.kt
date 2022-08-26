package com.artmaster.android.orthodoxcalendar.ui.init

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import kotlin.random.Random

@Preview
@Composable
fun Preview() {
    DisappearingTextView(title = "Какой-то очень длинный текст")
}


@Composable
fun DisappearingTextView(title: String, withReverse: Boolean = true, animDurationMs: Int = 6000) {

    val animStartBySymbols = remember {
        getAnimationOffsets(title.length, maxValue = animDurationMs / 3)
    }

    val infiniteTransition = rememberInfiniteTransition()
    val animValue by infiniteTransition.animateFloat(
        0f, animDurationMs.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animDurationMs),
            repeatMode = RepeatMode.Reverse
        )
    )

    val annotatedString = buildAnnotatedString {
        title.forEachIndexed { index, char ->
            val value = animStartBySymbols[index]
            val alpha = calculateAlpha(value, animValue, animDurationMs)
            val color = DefaultTextColor.copy(alpha = alpha)
            withStyle(style = SpanStyle(color = color)) {
                append(char)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = annotatedString, color = DefaultTextColor,
            fontSize = 35.sp,
            fontFamily = FontFamily(Font(R.font.decorated, FontWeight.Normal))
        )
    }
}

fun calculateAlpha(value: Int, animValue: Float, animDuration: Int): Float {
    val delta = 1 / (animDuration - value).toFloat()
    val inRange = (animValue < value + animDuration) && (animValue > value)
    return if (inRange) {
        (animValue - value) * delta
    } else if (animValue < value) {
        0f
    } else 1f
}

fun getAnimationOffsets(elementsCount: Int, maxValue: Int): List<Int> {
    val random = Random(elementsCount)
    return (0..elementsCount).map { random.nextInt(maxValue) }
}
