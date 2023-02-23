package uvis.irin.grape.categories.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uvis.irin.grape.categories.ui.model.UiCategory

private const val GRID_COLUMNS_COUNT = 2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesLoadedContent(
    modifier: Modifier = Modifier,
    categories: List<UiCategory>,
    isSynchronizing: Boolean,
    onCategoryCardClicked: (UiCategory) -> Unit,
) {
    Column {
        AnimatedVisibility(visible = isSynchronizing) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(GRID_COLUMNS_COUNT),
        ) {
            items(categories, key = { it.path }) { category ->
                CategoryCard(
                    modifier = Modifier.animateItemPlacement(),
                    category = category,
                    onCategoryCardClicked = onCategoryCardClicked,
                )
            }
        }
    }
}
