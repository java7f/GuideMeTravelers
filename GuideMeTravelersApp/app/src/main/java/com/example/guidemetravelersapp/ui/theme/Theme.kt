package com.example.guidemetravelersapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Teal200,
    primaryVariant = Teal700,
    secondary = MilitaryGreen200,
    secondaryVariant = DarkMilitaryGreen,
    onPrimary = Gray100,
    onSecondary = Color.White,
    background = Color.Unspecified,
    onBackground = Color.White,
    onSurface = Color.White,
    error = CancelRed,
    onError = CancelRed
)

private val LightColorPalette = lightColors(
    primary = Teal200,
    primaryVariant = Teal700,
    secondary = MilitaryGreen200,
    secondaryVariant = DarkMilitaryGreen,
    onPrimary = Gray200,
    onSecondary = Color.DarkGray,
    background = Color.White,
    onBackground = Gray200,
    onSurface = Color.Black,
    error = CancelRed,
    onError = CancelRed
)

@Composable
fun GuideMeTravelersAppTheme(
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