package uvis.irin.grape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uvis.irin.grape.core.ui.theme.GrapeTheme
import uvis.irin.grape.navigation.GrapeNavHost

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            GrapeTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()

                    GrapeNavHost(
                        navController = navController
                    )
                }
            }
        }
    }
}
