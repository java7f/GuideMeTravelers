package com.example.guidemetravelersapp.LoginView.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.guidemetravelersapp.ui.theme.Aquamarine200
import com.example.guidemetravelersapp.ui.theme.Gray200
import com.example.guidemetravelersapp.ui.theme.Pink200
import com.example.guidemetravelersapp.ui.theme.Pink700

private val DarkColorPalette = darkColors(
    primary = Pink200,
    primaryVariant = Pink700,
    secondary = Gray200,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Pink200,
    primaryVariant = Pink700,
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