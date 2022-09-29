@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)

package uvis.irin.grape.soundlist.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory

@Composable
fun SoundListContent(
    viewState: SoundListViewState,
    onSoundPressed: (sound: ResourceSound, context: Context) -> Unit,
    onSoundShareButtonPressed: (sound: ResourceSound, context: Context) -> Unit,
    onCategorySelected: (category: ResourceSoundCategory) -> Unit,
    onSubcategorySelected: (category: ResourceSoundCategory) -> Unit,
    onFavouriteButtonPressed: (sound: ResourceSound) -> Unit,
    onDisplayOnlyFavouritesButtonPressed: () -> Unit,
    onSoundSearchBarTextChanged: (String) -> Unit,
    onBackButtonPressed: (context: Context) -> Unit,
    onErrorSnackbarDismissed: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        when (viewState.showLoading) {
            true -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                )
            }
            false -> {
                LoadedSoundListContent(
                    viewState,
                    onSoundPressed = onSoundPressed,
                    onSoundShareButtonPressed = onSoundShareButtonPressed,
                    onCategorySelected = onCategorySelected,
                    onSubcategorySelected = onSubcategorySelected,
                    onFavouriteButtonPressed = onFavouriteButtonPressed,
                    onDisplayOnlyFavouritesButtonPressed = onDisplayOnlyFavouritesButtonPressed,
                    onSoundSearchBarTextChanged = onSoundSearchBarTextChanged,
                    onBackButtonPressed = onBackButtonPressed,
                    onErrorSnackbarDismissed = onErrorSnackbarDismissed
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadedSoundListContent(
    viewState: SoundListViewState,
    onSoundPressed: (sound: ResourceSound, context: Context) -> Unit,
    onSoundShareButtonPressed: (sound: ResourceSound, context: Context) -> Unit,
    onCategorySelected: (category: ResourceSoundCategory) -> Unit,
    onSubcategorySelected: (category: ResourceSoundCategory) -> Unit,
    onFavouriteButtonPressed: (sound: ResourceSound) -> Unit,
    onDisplayOnlyFavouritesButtonPressed: () -> Unit,
    onSoundSearchBarTextChanged: (String) -> Unit,
    onBackButtonPressed: (context: Context) -> Unit,
    onErrorSnackbarDismissed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    BackHandler {
        onBackButtonPressed(context)
    }

    SoundListSnackbar(
        errorMessage = viewState.errorMessage,
        snackbarHostState = snackbarHostState,
        onErrorSnackbarDismissed = onErrorSnackbarDismissed
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            SoundListTabBarSection(
                categories = viewState.categories,
                selectedCategory = viewState.selectedCategory,
                subcategories = viewState.subcategories,
                selectedSubcategory = viewState.selectedSubcategory,
                displayOnlyFavourites = viewState.displayOnlyFavourites,
                searchQuery = viewState.searchQuery,
                onCategorySelected = onCategorySelected,
                onSubcategorySelected = onSubcategorySelected,
                onDisplayOnlyFavouritesButtonPressed = onDisplayOnlyFavouritesButtonPressed,
                onSoundSearchBarTextChanged = onSoundSearchBarTextChanged,
            )
        },
        bottomBar = {
            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
            )
        }
    ) { paddingValues ->
        SoundSection(
            paddingValues = paddingValues,
            sounds = viewState.sounds,
            favouriteSounds = viewState.favouriteSounds,
            displayOnlyFavourites = viewState.displayOnlyFavourites,
            searchQuery = viewState.searchQuery,
            onSoundPressed = onSoundPressed,
            onSoundShareButtonPressed = onSoundShareButtonPressed,
            onFavouriteButtonPressed = onFavouriteButtonPressed,
        )
    }
}

@Composable
fun SoundSection(
    paddingValues: PaddingValues,
    sounds: List<ResourceSound>,
    favouriteSounds: List<ResourceSound>,
    displayOnlyFavourites: Boolean,
    searchQuery: String,
    onSoundPressed: (sound: ResourceSound, context: Context) -> Unit,
    onSoundShareButtonPressed: (sound: ResourceSound, context: Context) -> Unit,
    onFavouriteButtonPressed: (sound: ResourceSound) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(10.dp)
    ) {
        val filteredSounds = if (displayOnlyFavourites) sounds.filter {
            favouriteSounds.contains(it)
        } else sounds

        items(
            items = filteredSounds.filter {
                it.name.lowercase().contains(searchQuery.lowercase())
            }.sorted(),
            key = { item -> item.completePath },
        ) { sound ->
            Column(
                modifier = Modifier.animateItemPlacement()
            ) {
                SoundRow(
                    sound = sound,
                    isFavourite = favouriteSounds.contains(sound),
                    onSoundPressed = onSoundPressed,
                    onSoundShareButtonPressed = onSoundShareButtonPressed,
                    onFavouriteButtonPressed = onFavouriteButtonPressed,
                )

                Divider()
            }
        }
    }
}

@Composable
fun SoundRow(
    sound: ResourceSound,
    isFavourite: Boolean,
    onSoundPressed: (sound: ResourceSound, context: Context) -> Unit,
    onSoundShareButtonPressed: (sound: ResourceSound, context: Context) -> Unit,
    onFavouriteButtonPressed: (sound: ResourceSound) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        FilledTonalButton(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 6.dp),
            onClick = { onSoundPressed(sound, context) },
        ) {
            Text(
                text = sound.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall,
            )
        }

        IconToggleButton(
            checked = isFavourite,
            onCheckedChange = { onFavouriteButtonPressed(sound) }
        ) {
            @Suppress("MagicNumber")
            val size by animateDpAsState(
                targetValue = if (isFavourite) 26.dp else 24.dp,
                animationSpec = keyframes {
                    durationMillis = 250
                    24.dp at 0 with LinearOutSlowInEasing
                    26.dp at 15 with FastOutLinearInEasing
                    30.dp at 75
                    28.dp at 150
                }
            )

            Icon(
                imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                modifier = Modifier.size(size),
                contentDescription = null,
            )
        }

        IconButton(onClick = { onSoundShareButtonPressed(sound, context) }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun SoundListSnackbar(
    errorMessage: String?,
    snackbarHostState: SnackbarHostState,
    onErrorSnackbarDismissed: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    if (errorMessage != null) {
        LaunchedEffect(snackbarHostState) {
            coroutineScope.launch {
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = errorMessage,
                    duration = SnackbarDuration.Short,
                )

                when (snackbarResult) {
                    SnackbarResult.Dismissed -> {
                        onErrorSnackbarDismissed()
                    }
                    SnackbarResult.ActionPerformed -> {
                        onErrorSnackbarDismissed()
                    }
                }
            }
        }
    }
}

@Composable
fun SoundListTabBarSection(
    categories: List<ResourceSoundCategory>,
    selectedCategory: ResourceSoundCategory?,
    subcategories: List<ResourceSoundCategory>?,
    selectedSubcategory: ResourceSoundCategory?,
    displayOnlyFavourites: Boolean,
    searchQuery: String,
    onCategorySelected: (category: ResourceSoundCategory) -> Unit,
    onSubcategorySelected: (category: ResourceSoundCategory) -> Unit,
    onDisplayOnlyFavouritesButtonPressed: () -> Unit,
    onSoundSearchBarTextChanged: (String) -> Unit,
) {
    Column {
        FavouritesAndSearchRow(
            displayOnlyFavourites = displayOnlyFavourites,
            searchQuery = searchQuery,
            onDisplayOnlyFavouritesButtonPressed = onDisplayOnlyFavouritesButtonPressed,
            onSoundSearchBarTextChanged = onSoundSearchBarTextChanged
        )
        SoundListTabBar(
            categories = categories,
            selectedTabIndex = categories.indexOf(selectedCategory),
            onCategorySelected = onCategorySelected,
        )
        AnimatedVisibility(visible = subcategories != null) {
            subcategories?.let {
                SoundListTabBar(
                    categories = it,
                    selectedTabIndex = subcategories.indexOf(selectedSubcategory),
                    onCategorySelected = onSubcategorySelected
                )
            }
        }
    }
}

@Composable
fun FavouritesAndSearchRow(
    displayOnlyFavourites: Boolean,
    searchQuery: String,
    onDisplayOnlyFavouritesButtonPressed: () -> Unit,
    onSoundSearchBarTextChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        val bottomBorderColor = MaterialTheme.colorScheme.onSurfaceVariant

        DisplayOnlyFavouritesButton(
            displayOnlyFavourites = displayOnlyFavourites,
            onDisplayOnlyFavouritesButtonPressed = onDisplayOnlyFavouritesButtonPressed,
            bottomBorderColor = bottomBorderColor
        )
        SoundSearchBar(
            searchQuery = searchQuery,
            onSoundSearchBarTextChanged = onSoundSearchBarTextChanged,
            modifier = Modifier
                .padding(
                    WindowInsets.statusBars.asPaddingValues()
                )
                .weight(1f)
                .drawBehind {
                    val canvasHeight = this.size.height
                    val canvasWidth = this.size.width
                    drawLine(
                        brush = SolidColor(bottomBorderColor),
                        start = Offset(0f, canvasHeight),
                        end = Offset(canvasWidth / 2, canvasHeight),
                        strokeWidth = 5.dp.value
                    )
                }
        )
    }
}

@Composable
fun DisplayOnlyFavouritesButton(
    displayOnlyFavourites: Boolean,
    onDisplayOnlyFavouritesButtonPressed: () -> Unit,
    bottomBorderColor: Color
) {
    @Suppress("MagicNumber")
    val size by animateDpAsState(
        targetValue = if (displayOnlyFavourites) 26.dp else 24.dp,
        animationSpec = keyframes {
            durationMillis = 250
            24.dp at 0 with LinearOutSlowInEasing
            26.dp at 15 with FastOutLinearInEasing
            30.dp at 75
            28.dp at 150
        }
    )

    IconToggleButton(
        modifier = Modifier
            .padding(
                WindowInsets.statusBars.asPaddingValues()
            )
            .size(56.dp)
            .drawBehind {
                val canvasHeight = this.size.height
                val canvasWidth = this.size.width
                drawLine(
                    brush = SolidColor(bottomBorderColor),
                    start = Offset(0f, canvasHeight),
                    end = Offset(canvasWidth, canvasHeight),
                    strokeWidth = 6.dp.value,
                )

                @Suppress("MagicNumber")
                drawLine(
                    brush = SolidColor(bottomBorderColor),
                    start = Offset(canvasWidth, canvasHeight / 10),
                    end = Offset(canvasWidth, canvasHeight),
                    strokeWidth = 6.dp.value,
                )
            },
        checked = displayOnlyFavourites,
        onCheckedChange = { onDisplayOnlyFavouritesButtonPressed() },
    ) {
        Icon(
            imageVector = if (displayOnlyFavourites) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            modifier = Modifier.size(size),
            contentDescription = null,
        )
    }
}

@Composable
fun SoundSearchBar(
    searchQuery: String,
    onSoundSearchBarTextChanged: (String) -> Unit,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = searchQuery,
        onValueChange = onSoundSearchBarTextChanged,
        placeholder = { Text(text = "Search...") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = { onSoundSearchBarTextChanged("") }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null)
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.surface)
    )
}

@Composable
fun SoundListTabBar(
    categories: List<ResourceSoundCategory>,
    selectedTabIndex: Int,
    onCategorySelected: (category: ResourceSoundCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp,
        modifier = modifier,
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                text = { Text(text = category.name) },
                selected = selectedTabIndex == index,
                onClick = { onCategorySelected(category) },
            )
        }
    }
}
