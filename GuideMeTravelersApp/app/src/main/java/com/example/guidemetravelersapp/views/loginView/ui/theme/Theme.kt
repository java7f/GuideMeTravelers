package com.example.guidemetravelersapp.views.loginView.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.guidemetravelersapp.ui.theme.Gray200
import com.example.guidemetravelersapp.ui.theme.Teal200
import com.example.guidemetravelersapp.ui.theme.Teal700

private val DarkColorPalette = darkColors(
    primary = Teal200,
    primaryVariant = Teal700,
    secondary = Gray200,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Teal200,
    primaryVariant = Teal700,
    secondary = Gray200,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
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