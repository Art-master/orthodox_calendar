package com.artmaster.android.orthodoxcalendar.ui.init_page.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.AnnotatedString
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
fun DisappearingTextPreview() {
    DisappearingTextAnimation(title = "Какой-то очень длинный текст", 6000) {
        Text(
            textAlign = TextAlign.Center,
            text = it, color = DefaultTextColor,
            fontSize = 35.sp,
            fontFamily = FontFamily(Font(R.font.decorated, FontWeight.Normal))
        )
    }
}

@Composable
fun DisappearingTextAnimation(
    title: String,
    animDurationMs: Int,
    revertValue: Boolean = false,
    withReverse: Boolean = true,
    onComplete: (() -> Unit)? = null,
    content: @Composable (string: AnnotatedString) -> Unit
) {

    val animStartBySymbols = remember {
        val maxValue = (animDurationMs / 1.3).toInt()
        getAnimationOffsets(title.length, maxValue = maxValue)
    }

    var targetValue by remember { mutableStateOf(0f) }

    val animation by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = repeatable(
            animation = tween(durationMillis = animDurationMs),
            repeatMode = RepeatMode.Reverse,
            iterations = if (withReverse) 2 else 1
        ),
        finishedListener = {
            onComplete?.invoke()
        }
    )

    // For animation launch
    SideEffect { targetValue = animDurationMs.toFloat() }

    val annotatedString = buildAnnotatedString {
        title.forEachIndexed { index, char ->

            val value = animStartBySymbols[index]
            val alpha = calculateAlpha(value, animation, animDurationMs)
            val color = DefaultTextColor.copy(alpha = if (revertValue) 1f - alpha else alpha)

            withStyle(style = SpanStyle(color = color)) {
                append(char)
            }
        }
    }

    content.invoke(annotatedString)
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
