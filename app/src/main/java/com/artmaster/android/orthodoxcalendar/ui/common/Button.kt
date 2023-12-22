package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.ui.theme.ButtonBackground
import com.artmaster.android.orthodoxcalendar.ui.theme.ButtonContent
import com.artmaster.android.orthodoxcalendar.ui.theme.DisabledButtonBackground
import com.artmaster.android.orthodoxcalendar.ui.theme.DisabledButtonContent

@Composable
fun StyledButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    title: String,
    onClick: (() -> Unit)? = null
) {
    val onClickRemembered by rememberUpdatedState { onClick?.invoke() ?: Unit }

    Button(
        modifier = Modifier
            .padding(start = 5.dp)
            .then(modifier),
        enabled = enabled,
        onClick = onClickRemembered,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ButtonBackground,
            contentColor = ButtonContent,
            disabledBackgroundColor = DisabledButtonBackground,
            disabledContentColor = DisabledButtonContent
        )
    ) {
        StyledText(title = title)
    }
}