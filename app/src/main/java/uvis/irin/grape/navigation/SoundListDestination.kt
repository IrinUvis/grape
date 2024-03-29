package uvis.irin.grape.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import uvis.irin.grape.core.extension.withForwardSlashesReplacedByDashes
import uvis.irin.grape.soundlist.ui.SoundListScreen

const val SOUND_LIST_ARG = "soundsCategory"

object SoundListDestination : AppDestination("soundlist/{$SOUND_LIST_ARG}")

fun NavGraphBuilder.soundListScreen(
    navController: NavController,
) {
    composable(
        route = SoundListDestination.route,
        arguments = listOf(navArgument(SOUND_LIST_ARG) { type = NavType.StringType })
    ) {
        SoundListScreen(
            navigateUp = { navController.navigateUp() },
            navigateToCategories = { path ->
                navController.navigateToCategories(path.withForwardSlashesReplacedByDashes()) {
                    popUpTo(SoundListDestination.route) { inclusive = true }
                }
            }
        )
    }
}

fun NavController.navigateToSoundList(
    path: String,
    navOptions: NavOptions? = null,
) {
    navigate(
        SoundListDestination.route.replace(
            oldValue = "{$SOUND_LIST_ARG}",
            newValue = path,
        ),
        navOptions = navOptions,
    )
}
