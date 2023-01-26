package uvis.irin.grape.categories.ui.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uvis.irin.grape.categories.ui.model.UiCategory

private const val GRID_COLUMNS_COUNT = 2

@Composable
fun CategoriesLoadedContent(
    modifier: Modifier = Modifier,
    categories: List<UiCategory>,
    onCategoryCardClicked: (UiCategory) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(GRID_COLUMNS_COUNT),
    ) {
        items(categories) { category ->
            CategoryCard(
                category = category,
                onCategoryCardClicked = onCategoryCardClicked,
            )
        }
    }
}
