package com.artmaster.android.orthodoxcalendar.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val localRippleEnabled = compositionLocalOf { true }
    MaterialTheme {
        CompositionLocalProvider(localRippleEnabled provides false) {
            content()
        }
    }
}