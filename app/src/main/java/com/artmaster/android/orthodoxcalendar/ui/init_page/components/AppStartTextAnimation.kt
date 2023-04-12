package com.artmaster.android.orthodoxcalendar.ui.init_page.components

import android.view.MotionEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import kotlinx.coroutines.delay
import java.util.*

const val USER_TOUCH_STOP_ANIM_TIME_MS = 500L

@Preview
@Composable
fun Preview() {
    AppStartTextAnimation(duration = 6000, 50)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppStartTextAnimation(duration: Int, resIndex: Int?, onComplete: () -> Unit = {}) {

    if (duration == 0) {
        onComplete()
        return
    }

    val strings = stringArrayResource(R.array.loading_strings_array)

    val title = remember {
        val num = Random().nextInt(strings.size.dec())
        strings[resIndex ?: num]
    }

    var userTouchScreen by remember { mutableStateOf(false) }
    var needToPauseAnimation by remember { mutableStateOf(false) }
    var firstComplete by remember { mutableStateOf(false) }
    val onFirstPartComplete = remember { { firstComplete = true } }

    LaunchedEffect(userTouchScreen) {
        needToPauseAnimation = if (userTouchScreen) {
            delay(USER_TOUCH_STOP_ANIM_TIME_MS)
            userTouchScreen
        } else false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> userTouchScreen = true
                    MotionEvent.ACTION_UP -> userTouchScreen = false
                }
                true
            },
        contentAlignment = Alignment.Center
    ) {
        if (firstComplete.not() || needToPauseAnimation) {
            DisappearingTextAnimation(
                title = title,
                animDurationMs = duration,
                withReverse = false,
                onComplete = onFirstPartComplete
            ) {
                AppearanceText(string = it)
            }
        } else {
            DisappearingTextAnimation(
                title = title,
                animDurationMs = duration,
                withReverse = false,
                revertValue = true,
                onComplete = onComplete
            ) {
                AppearanceText(string = it)
            }
        }

    }
}


@Composable
fun AppearanceText(string: AnnotatedString) {
    Text(
        textAlign = TextAlign.Center,
        style = TextStyle(color = Color.Transparent),
        text = string,
        color = DefaultTextColor,
        fontSize = 35.sp,
        fontFamily = FontFamily(Font(R.font.decorated, FontWeight.Normal))
    )
}