package uvis.irin.grape.categories.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uvis.irin.grape.core.ui.components.GrapeLoadingError

@Composable
fun CategoriesLoadingErrorContent(
    modifier: Modifier = Modifier,
    onRetryButtonClicked: () -> Unit,
    errorMessage: String,
) {
    GrapeLoadingError(
        modifier = modifier,
        onRetryButtonClicked = onRetryButtonClicked,
        errorMessage = errorMessage
    )
}