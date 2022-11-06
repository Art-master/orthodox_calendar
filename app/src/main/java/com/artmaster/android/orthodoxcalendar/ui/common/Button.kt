package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StyledButton(modifier: Modifier = Modifier, title: String, onClick: () -> Unit = {}) {
    Button(
        modifier = Modifier
            .padding(start = 5.dp)
            .then(modifier),
        onClick = onClick
    ) {
        StyledText(title = title)
    }
}