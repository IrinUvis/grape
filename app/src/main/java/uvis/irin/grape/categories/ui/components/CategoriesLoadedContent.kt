package uvis.irin.grape.categories.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import uvis.irin.grape.R
import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.core.constants.mediumPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesLoadedContent(
    modifier: Modifier = Modifier,
    categories: List<UiCategory>,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
    ) {
        items(categories) { category ->
            Card(
                modifier = Modifier.padding(
                    horizontal = mediumPadding,
                    vertical = mediumPadding
                ),
                onClick = { }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize(),
                        painter = painterResource(R.drawable.test),
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.6f)),
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            text = category.name
                        )
                    }
//                    Box(
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .fillMaxSize()
//                            .background(Color.Black.copy(alpha = 0.2f)),
//                    ) {
//                        Text(
//                            modifier = Modifier
//                                .align(Alignment.Center),
//                            text = category.name
//                        )
//                    }
                }
            }
        }
    }
}
