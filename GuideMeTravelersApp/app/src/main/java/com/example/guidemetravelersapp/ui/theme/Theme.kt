package com.example.guidemetravelersapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Pink200,
    primaryVariant = Pink700,
    secondary = MilitaryGreen200,
    secondaryVariant = DarkMilitaryGreen,
    onPrimary = Gray100,
    onSecondary = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Pink200,
    primaryVariant = Pink700,
    secondary = MilitaryGreen200,
    secondaryVariant = DarkMilitaryGreen,
    onPrimary = Gray200,
    onSecondary = Color.Black,
    background = Color.White,
    onBackground = Gray200,
    onSurface = Color.Black
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