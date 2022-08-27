package com.artmaster.android.orthodoxcalendar.ui.init.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.init.DisappearingTextView
import java.util.*

@Preview
@Composable
fun Preview() {
    AppStartTextAnimation(time = 6000)
}

@Composable
fun AppStartTextAnimation(time: Int, onComplete: () -> Unit = {}) {

    val strings = stringArrayResource(R.array.loading_strings_array)

    val title = remember {
        val num = Random().nextInt(strings.size.dec())
        strings[num]
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DisappearingTextView(
            title = title,
            animDurationMs = time,
            onComplete = onComplete
        )
    }
}