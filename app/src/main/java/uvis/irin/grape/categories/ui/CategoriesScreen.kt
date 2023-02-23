package uvis.irin.grape.categories.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateToSounds: (String) -> Unit,
    navigateToCategories: (String) -> Unit,
) {
    val viewState = viewModel.viewState.collectAsState()

    CategoriesContent(
        viewState = viewState.value,
        onNavigationIconClicked = navigateUp,
        onCategoryCardClicked = { category ->
            if (category.isFinalCategory) {
                navigateToSounds(category.path)
            } else {
                navigateToCategories(category.path)
            }
        },
        onRetryButtonClicked = viewModel::retryLoadingCategories
    )
}
