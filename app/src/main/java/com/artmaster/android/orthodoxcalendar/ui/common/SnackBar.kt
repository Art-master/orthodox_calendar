package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.ui.theme.SnackBarBackground

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_3)
fun StyledSnackBarPreview() {
    StyledSnackBar(message = "Какое-то сообщение")
}


@Composable
fun StyledSnackBar(
    message: String,
    isRtl: Boolean = true,
    containerColor: Color = SnackBarBackground
) = Snackbar(backgroundColor = containerColor, shape = RoundedCornerShape(6.dp)) {
    CompositionLocalProvider(
        LocalLayoutDirection provides
                if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    ) {
        Row {
            StyledText(title = message)
        }
    }
}