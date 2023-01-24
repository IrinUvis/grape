package uvis.irin.grape.categories.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uvis.irin.grape.categories.ui.components.CategoriesLoadedContent
import uvis.irin.grape.categories.ui.components.CategoriesLoadingContent
import uvis.irin.grape.categories.ui.components.CategoriesTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesContent(
    viewState: CategoriesViewState,
    onNavigationIconClicked: () -> Unit,
    navigateToSounds: () -> Unit,
) {
    Scaffold(
        topBar = {
            CategoriesTopAppBar(
                category = viewState.category,
                isTopLevelCategory = true,
                onNavigationIconClicked = onNavigationIconClicked,
            )
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier.padding(paddingValues),
            targetState = viewState.isLoaded,
        ) { isLoaded ->
            when (isLoaded) {
                true -> {
                    CategoriesLoadedContent(
                        categories = viewState.categories!!,
                    )
//                    Box(modifier = Modifier.fillMaxSize()) {
//                        Button(
//                            modifier = Modifier.align(Alignment.Center),
//                            onClick = navigateToSounds
//                        ) {
//                            Text(text = "jail")
//                        }
//                    }
                }
                false -> {
                    CategoriesLoadingContent()
                }
            }
        }
    }

}
