package uvis.irin.grape.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost


@Composable
fun GrapeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = CategoriesDestination.route
    ) {
        categoriesScreen(
            navController = navController,
        )
        soundListScreen(
            navController = navController
        )
    }
}

sealed class AppDestination(
    val route: String,
)
