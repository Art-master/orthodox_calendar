package com.artmaster.android.orthodoxcalendar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.c23v.ui.theme.Shapes

private val DarkColorPalette = darkColors(
    primary = Cian200,
    primaryVariant = Cian400,
    secondary = Teal200,
    background = Gray700,
)

private val LightColorPalette = lightColors(
    primary = Cian500,
    primaryVariant = Cian400,
    secondary = Teal200,
    background = Brown50,
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ApplicationsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}