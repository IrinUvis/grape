@file:Suppress("MagicNumber")

package uvis.irin.grape.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val light_primary = Color(0xFF536500)
private val light_onPrimary = Color(0xFFffffff)
private val light_primaryContainer = Color(0xFFd5ed7e)
private val light_onPrimaryContainer = Color(0xFF171e00)
private val light_secondary = Color(0xFF5c6146)
private val light_onSecondary = Color(0xFFffffff)
private val light_secondaryContainer = Color(0xFFe0e6c3)
private val light_onSecondaryContainer = Color(0xFF191d08)
private val light_tertiary = Color(0xFF3a665d)
private val light_onTertiary = Color(0xFFffffff)
private val light_tertiaryContainer = Color(0xFFbcece0)
private val light_onTertiaryContainer = Color(0xFF00201a)
private val light_error = Color(0xFFba1b1b)
private val light_errorContainer = Color(0xFFffdad4)
private val light_onError = Color(0xFFffffff)
private val light_onErrorContainer = Color(0xFF410001)
private val light_background = Color(0xFFfefcf3)
private val light_onBackground = Color(0xFF1b1c17)
private val light_surface = Color(0xFFfefcf3)
private val light_onSurface = Color(0xFF1b1c17)
private val light_surfaceVariant = Color(0xFFe3e3d3)
private val light_onSurfaceVariant = Color(0xFF46483b)
private val light_outline = Color(0xFF77786b)
private val light_inverseOnSurface = Color(0xFFf3f1e8)
private val light_inverseSurface = Color(0xFF31312c)
private val light_inversePrimary = Color(0xFFb9d065)

private val dark_primary = Color(0xFFb9d065)
private val dark_onPrimary = Color(0xFF293500)
private val dark_primaryContainer = Color(0xFF3d4d00)
private val dark_onPrimaryContainer = Color(0xFFd5ed7e)
private val dark_secondary = Color(0xFFc5caa9)
private val dark_onSecondary = Color(0xFF2e331c)
private val dark_secondaryContainer = Color(0xFF444930)
private val dark_onSecondaryContainer = Color(0xFFe0e6c3)
private val dark_tertiary = Color(0xFFa1d0c5)
private val dark_onTertiary = Color(0xFF03372f)
private val dark_tertiaryContainer = Color(0xFF214e46)
private val dark_onTertiaryContainer = Color(0xFFbcece0)
private val dark_error = Color(0xFFffb4a9)
private val dark_errorContainer = Color(0xFF930006)
private val dark_onError = Color(0xFF680003)
private val dark_onErrorContainer = Color(0xFFffdad4)
private val dark_background = Color(0xFF1b1c17)
private val dark_onBackground = Color(0xFFe5e3db)
private val dark_surface = Color(0xFF1b1c17)
private val dark_onSurface = Color(0xFFe5e3db)
private val dark_surfaceVariant = Color(0xFF46483b)
private val dark_onSurfaceVariant = Color(0xFFc7c8b8)
private val dark_outline = Color(0xFF909283)
private val dark_inverseOnSurface = Color(0xFF1b1c17)
private val dark_inverseSurface = Color(0xFFe5e3db)
private val dark_inversePrimary = Color(0xFF536500)

// private val seed = Color(0xFF92a451)
// private val error = Color(0xFFba1b1b)

val LightThemeColors = lightColorScheme(
    primary = light_primary,
    onPrimary = light_onPrimary,
    primaryContainer = light_primaryContainer,
    onPrimaryContainer = light_onPrimaryContainer,
    secondary = light_secondary,
    onSecondary = light_onSecondary,
    secondaryContainer = light_secondaryContainer,
    onSecondaryContainer = light_onSecondaryContainer,
    tertiary = light_tertiary,
    onTertiary = light_onTertiary,
    tertiaryContainer = light_tertiaryContainer,
    onTertiaryContainer = light_onTertiaryContainer,
    error = light_error,
    onError = light_onError,
    errorContainer = light_errorContainer,
    onErrorContainer = light_onErrorContainer,
    background = light_background,
    onBackground = light_onBackground,
    surface = light_surface,
    onSurface = light_onSurface,
    surfaceVariant = light_surfaceVariant,
    onSurfaceVariant = light_onSurfaceVariant,
    outline = light_outline,
    inversePrimary = light_inversePrimary,
    inverseSurface = light_inverseSurface,
    inverseOnSurface = light_inverseOnSurface,
)

val DarkThemeColors = darkColorScheme(
    primary = dark_primary,
    onPrimary = dark_onPrimary,
    primaryContainer = dark_primaryContainer,
    onPrimaryContainer = dark_onPrimaryContainer,
    secondary = dark_secondary,
    onSecondary = dark_onSecondary,
    secondaryContainer = dark_secondaryContainer,
    onSecondaryContainer = dark_onSecondaryContainer,
    tertiary = dark_tertiary,
    onTertiary = dark_onTertiary,
    tertiaryContainer = dark_tertiaryContainer,
    onTertiaryContainer = dark_onTertiaryContainer,
    error = dark_error,
    onError = dark_onError,
    errorContainer = dark_errorContainer,
    onErrorContainer = dark_onErrorContainer,
    background = dark_background,
    onBackground = dark_onBackground,
    surface = dark_surface,
    onSurface = dark_onSurface,
    surfaceVariant = dark_surfaceVariant,
    onSurfaceVariant = dark_onSurfaceVariant,
    outline = dark_outline,
    inversePrimary = dark_inversePrimary,
    inverseSurface = dark_inverseSurface,
    inverseOnSurface = dark_inverseOnSurface,
)

val DarkMD2Colors = darkColors(
    primary = DarkThemeColors.primary,
    secondary = DarkThemeColors.secondary,
    background = DarkThemeColors.background,
    surface = DarkThemeColors.surface,
    error = DarkThemeColors.error,
    onPrimary = DarkThemeColors.onPrimary,
    onSecondary = DarkThemeColors.onSecondary,
    onBackground = DarkThemeColors.onBackground,
    onSurface = DarkThemeColors.onSurface,
    onError = DarkThemeColors.onError,
)

val LightMD2Colors = lightColors(
    primary = LightThemeColors.primary,
    secondary = LightThemeColors.secondary,
    background = LightThemeColors.background,
    surface = LightThemeColors.surface,
    error = LightThemeColors.error,
    onPrimary = LightThemeColors.onPrimary,
    onSecondary = LightThemeColors.onSecondary,
    onBackground = LightThemeColors.onBackground,
    onSurface = LightThemeColors.onSurface,
    onError = LightThemeColors.onError,
)
