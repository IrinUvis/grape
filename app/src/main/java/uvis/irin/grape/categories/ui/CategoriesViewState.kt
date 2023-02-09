package uvis.irin.grape.categories.ui

import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.core.ui.helpers.UiText

data class CategoriesViewState(
    val category: UiCategory,
    val categoriesLoadingState: CategoriesLoadingState = CategoriesLoadingState.Loading,
    val isSynchronizing: Boolean = false,
    val categories: List<UiCategory>? = null,
    val errorMessage: UiText? = null,
)

enum class CategoriesLoadingState {
    Loading,
    Loaded,
    LoadingError,
}
