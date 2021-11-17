package com.example.guidemetravelersapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.guidemetravelersapp.R

val RobotoCondensed = FontFamily(
    Font(R.font.yaldevi_regular),
    Font(R.font.yaldevi_bold, FontWeight.Bold),
    Font(R.font.yaldevi_light, FontWeight.Light)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = RobotoCondensed,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    h1 = TextStyle(
        fontFamily = RobotoCondensed,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = RobotoCondensed,
    fontWeight = FontWeight.Light,
    fontSize = 60.sp,
    letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontFamily = RobotoCondensed,
    fontWeight = FontWeight.Normal,
    fontSize = 48.sp,
    letterSpacing = 0.sp
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        letterSpacing = 0.25.sp,
        fontFamily = RobotoCondensed
    ),
    h5 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
    letterSpacing = 0.sp,
        fontFamily = RobotoCondensed
    ),
    h6 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp,
        fontFamily = RobotoCondensed
    ),
    subtitle1 = TextStyle(
        fontFamily = RobotoCondensed,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = RobotoCondensed,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 0.1.sp
    ),
    body2 = TextStyle(
        fontFamily = RobotoCondensed,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = 0.25.sp
    ),
    button = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 1.25.sp,
        fontFamily = RobotoCondensed
    ),
    caption = TextStyle(
        fontFamily = RobotoCondensed,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    letterSpacing = 0.4.sp
    ),
    overline = TextStyle(
        fontFamily = RobotoCondensed,
    fontWeight = FontWeight.Normal,
    fontSize = 10.sp,
    letterSpacing = 0.5.sp
    )

    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)