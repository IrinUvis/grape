package uvis.irin.grape.categories.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uvis.irin.grape.categories.ui.components.CategoriesLoadedContent
import uvis.irin.grape.categories.ui.components.CategoriesLoadingContent
import uvis.irin.grape.categories.ui.components.CategoriesLoadingErrorContent
import uvis.irin.grape.categories.ui.components.CategoriesTopAppBar
import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.core.ui.helpers.getString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesContent(
    viewState: CategoriesViewState,
    onNavigationIconClicked: () -> Unit,
    onCategoryCardClicked: (UiCategory) -> Unit,
    onRetryButtonClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            CategoriesTopAppBar(
                category = viewState.category,
                onNavigationIconClicked = onNavigationIconClicked,
            )
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier.padding(paddingValues),
            targetState = viewState.categoriesLoadingState,
        ) { categoriesLoadingState ->
            when (categoriesLoadingState) {
                CategoriesLoadingState.Loading -> {
                    CategoriesLoadingContent()
                }
                CategoriesLoadingState.Loaded -> {
                    viewState.categories?.let { categories ->
                        CategoriesLoadedContent(
                            categories = categories,
                            onCategoryCardClicked = onCategoryCardClicked,
                        )
                    }
                }
                CategoriesLoadingState.LoadingError -> {
                    viewState.errorMessage?.let { errorMessage ->
                        CategoriesLoadingErrorContent(
                            onRetryButtonClicked = onRetryButtonClicked,
                            errorMessage = errorMessage.getString()
                        )
                    }
                }
            }
        }
    }

}
