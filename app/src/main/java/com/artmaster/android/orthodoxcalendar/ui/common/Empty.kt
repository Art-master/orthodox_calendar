package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_3)
fun EmptyPreview() {
    Empty()
}

@Composable
fun Empty() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            color = DefaultTextColor,
            text = stringResource(id = R.string.nothing_found),
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old)),
            textAlign = TextAlign.Center
        )
    }
}