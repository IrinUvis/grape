package uvis.irin.grape.categories.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()

    CategoriesContent(
        viewState = viewState.value,
    )
}
