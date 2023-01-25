package uvis.irin.grape.categories.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.core.constants.mediumPadding
import uvis.irin.grape.core.constants.smallPadding

private const val GRID_COLUMNS_COUNT = 2

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoriesLoadedContent(
    modifier: Modifier = Modifier,
    categories: List<UiCategory>,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(GRID_COLUMNS_COUNT),
    ) {
        items(categories) { category ->
            OutlinedCard(
                modifier = Modifier.padding(
                    horizontal = mediumPadding,
                    vertical = mediumPadding
                ),
                onClick = { }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.aspectRatio(1f),
                        bitmap = category.bitmap.asImageBitmap(),
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                    )

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                modifier = Modifier
                                    .padding(smallPadding)
                                    .fillMaxWidth()
                                    .basicMarquee(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge,
                                text = category.name
                            )
                        }
                    }
                }
            }
        }
    }
}


//                Box(modifier = Modifier.fillMaxSize()) {
//                    Image(
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .fillMaxSize(),
//                        painter = painterResource(R.drawable.test),
//                        contentScale = ContentScale.Crop,
//                        contentDescription = "",
//                    )
//                    Box(
//                        modifier = Modifier
//                            .matchParentSize()
//                            .background(Color.Black.copy(alpha = 0.4f)),
//                    ) {
//                        Text(
//                            modifier = Modifier
//                                .align(Alignment.Center),
//                            color = Color.White,
//                            style = MaterialTheme.typography.headlineMedium,
//                            text = category.name
//                        )
//                    }
//                }

//                Box(modifier = Modifier.fillMaxSize()) {
//                    Image(
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .fillMaxSize(),
//                        painter = painterResource(R.drawable.test),
//                        contentScale = ContentScale.Crop,
//                        contentDescription = "",
//                    )
//                    Text(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .align(Alignment.BottomCenter).background(Color.Black.copy(alpha = 0.3f)),
//                        color = Color.White,
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.headlineMedium,
//                        text = category.name
//                    )
//                }

//                Box(modifier = Modifier.fillMaxSize()) {
//                    Image(
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .fillMaxSize(),
//                        painter = painterResource(R.drawable.test),
//                        contentScale = ContentScale.Crop,
//                        contentDescription = "",
//                    )
//                    Text(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .align(Alignment.BottomCenter)
//                            .background(
//                                Brush.verticalGradient(
//                                    0F to Color.Transparent,
//                                    .5F to Color.Black.copy(alpha = 0.4F),
//                                    1F to Color.Black.copy(alpha = 0.8F)
//                                )
//                            ),
//                        color = Color.White,
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.headlineMedium,
//                        text = category.name
//                    )
//                }
