package uvis.irin.grape.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import uvis.irin.grape.categories.ui.CategoriesScreen
import uvis.irin.grape.core.extension.withForwardSlashesReplacedByDashes

const val CATEGORIES_ARG = "parentCategory"
const val INITIAL_CATEGORIES_ARG = "sounds"

object CategoriesDestination : AppDestination("categories/{$CATEGORIES_ARG}")

fun NavGraphBuilder.categoriesScreen(
    navController: NavController,
) {
    composable(
        CategoriesDestination.route,
        arguments = listOf(
            navArgument(CATEGORIES_ARG) {
                type = NavType.StringType
                defaultValue = INITIAL_CATEGORIES_ARG
            }
        )
    ) {
        CategoriesScreen(
            navigateToSounds = {
                navController.navigateToSoundList("sounds/01_jail".withForwardSlashesReplacedByDashes())
            }
        )
    }
}

fun NavController.navigateToCategories(
    path: String,
    navOptions: NavOptions? = null,
) {
    navigate(
        CategoriesDestination.route.replace(
            oldValue = "{$CATEGORIES_ARG}",
            newValue = path,
        ),
        navOptions = navOptions,
    )
}

fun NavController.navigateToCategories(
    path: String,
    builder: (NavOptionsBuilder.() -> Unit),
) {
    navigateToCategories(
        path = path,
        navOptions(builder),
    )
}
