package uvis.irin.grape.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import uvis.irin.grape.core.constants.ANDROID_12_SDK_INT

/**
 * Theme for the app.
 * It supports dynamic theming if the app is run on device running Android 12 or above.
 * Otherwise it uses default Dark or Light colorScheme.
 */
@Suppress("NewApi")
@Composable
fun GrapeTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    dynamic: Boolean = isAtLeastAndroid12(),
    content: @Composable() () -> Unit
) {
    val colors = when {
        dynamic && isDark -> dynamicDarkColorScheme(LocalContext.current)
        dynamic && !isDark -> dynamicLightColorScheme(LocalContext.current)
        isDark -> DarkThemeColors
        else -> LightThemeColors
    }

    androidx.compose.material.MaterialTheme(
        colors = if (isDark) DarkMD2Colors else LightMD2Colors,
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = AppTypography,
            content = content
        )
    }
}

private fun isAtLeastAndroid12(): Boolean {
    return Build.VERSION.SDK_INT >= ANDROID_12_SDK_INT
}
