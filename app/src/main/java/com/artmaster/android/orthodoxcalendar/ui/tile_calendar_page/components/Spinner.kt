package com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.artmaster.android.orthodoxcalendar.R

@Preview
@Composable
fun PreviewSpinner() {
    Spinner()
}

@Composable
fun Spinner(isProcessing: Boolean = true) {

    // Allow resume on rotation
    var currentRotation by remember { mutableStateOf(0f) }

    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            // Infinite repeatable rotation
            rotation.animateTo(
                targetValue = currentRotation + 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            ) {
                currentRotation = value
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        Image(
            modifier = Modifier.rotate(rotation.value),
            painter = painterResource(id = R.drawable.ring),
            contentDescription = ""
        )
        Image(
            painter = painterResource(id = R.drawable.cross2),
            contentDescription = ""
        )
    }
}