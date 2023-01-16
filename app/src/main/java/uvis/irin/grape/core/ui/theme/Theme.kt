package uvis.irin.grape.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Theme for the app.
 * It supports dynamic theming if the app is run on device running Android 12 or above.
 * Otherwise it uses default Dark or Light colorScheme.
 */
@Composable
fun GrapeTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    dynamic: Boolean = isAtLeastAndroid12(),
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        dynamic && isDark -> dynamicDarkColorScheme(LocalContext.current)
        dynamic && !isDark -> dynamicLightColorScheme(LocalContext.current)
        isDark -> DarkThemeColors
        else -> LightThemeColors
    }

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

private fun isAtLeastAndroid12() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
