package uvis.irin.grape.categories.ui

import uvis.irin.grape.categories.ui.model.UiCategory

data class CategoriesViewState(
    val categories: List<UiCategory>? = null,
    val isLoaded: Boolean = false,
)
