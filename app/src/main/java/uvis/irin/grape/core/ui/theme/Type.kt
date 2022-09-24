@file:Suppress("MagicNumber")

package uvis.irin.grape.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import uvis.irin.grape.R

val Bitter = FontFamily(
    Font(
        resId = R.font.bitter_regular,
    ),
    Font(
        resId = R.font.bitter_italic,
        style = FontStyle.Italic,
    ),
    Font(
        resId = R.font.bitter_extralight,
        weight = FontWeight.ExtraLight,
    ),
    Font(
        resId = R.font.bitter_extralightitalic,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Italic,
    ),
    Font(
        resId = R.font.bitter_light,
        weight = FontWeight.Light,
    ),
    Font(
        resId = R.font.bitter_lightitalic,
        weight = FontWeight.Light,
        style = FontStyle.Italic,
    ),
    Font(
        resId = R.font.bitter_semibold,
        weight = FontWeight.SemiBold,
    ),
    Font(
        resId = R.font.bitter_semibolditalic,
        weight = FontWeight.SemiBold,
        style = FontStyle.Italic,
    ),
    Font(
        resId = R.font.bitter_bold,
        weight = FontWeight.Bold,
    ),
    Font(
        resId = R.font.bitter_bolditalic,
        weight = FontWeight.Bold,
        style = FontStyle.Italic,
    ),
    Font(
        resId = R.font.bitter_extrabold,
        weight = FontWeight.ExtraBold,
    ),
    Font(
        resId = R.font.bitter_extrabolditalic,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Italic,
    ),
    Font(
        resId = R.font.bitter_medium,
        weight = FontWeight.Medium,
    ),
    Font(
        resId = R.font.bitter_mediumitalic,
        weight = FontWeight.Medium,
        style = FontStyle.Italic,
    ),
    Font(
        resId = R.font.bitter_black,
        weight = FontWeight.Black,
    ),
    Font(
        resId = R.font.bitter_blackitalic,
        weight = FontWeight.Black,
        style = FontStyle.Italic,
    ),
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.W400,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.W400,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.W400,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.W400,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.W400,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.W400,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.W400,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)
