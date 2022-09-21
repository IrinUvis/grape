@file:OptIn(
    ExperimentalMaterial3Api::class
)

package uvis.irin.grape.soundlist.ui

import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uvis.irin.grape.core.ui.theme.GrapeTheme
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory

@Composable
fun SoundListContent(
    viewState: SoundListViewState,
    onSoundPressed: (sound: Sound, context: Context) -> Unit,
    onSoundShareButtonPressed: (sound: Sound, context: Context) -> Unit,
    onCategorySelected: (category: SoundCategory) -> Unit,
    onSubcategorySelected: (category: SoundCategory) -> Unit,
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
    onSoundPressed: (sound: Sound, context: Context) -> Unit,
    onSoundShareButtonPressed: (sound: Sound, context: Context) -> Unit,
    onCategorySelected: (category: SoundCategory) -> Unit,
    onSubcategorySelected: (category: SoundCategory) -> Unit,
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
            Column {
                SoundSectionTabBar(
                    categories = viewState.categories,
                    selectedTabIndex = viewState.categories.indexOf(viewState.selectedCategory),
                    onCategorySelected = onCategorySelected,
                    modifier = Modifier.padding(
                        WindowInsets.statusBars.asPaddingValues()
                    )
                )
                AnimatedVisibility(visible = viewState.subcategories != null) {
                    viewState.subcategories?.let {
                        SoundSectionTabBar(
                            categories = it,
                            selectedTabIndex = viewState.subcategories.indexOf(viewState.selectedSubcategory),
                            onCategorySelected = onSubcategorySelected
                        )
                    }
                }
            }
        },
        bottomBar = {
            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
        ) {
            items(viewState.sounds) { sound ->
                SoundRow(
                    sound = sound,
                    onSoundPressed = onSoundPressed,
                    onSoundShareButtonPressed = onSoundShareButtonPressed,
                )

                Divider()
            }
        }
    }
}

@Composable
fun SoundRow(
    sound: Sound,
    onSoundPressed: (sound: Sound, context: Context) -> Unit,
    onSoundShareButtonPressed: (sound: Sound, context: Context) -> Unit
) {
    val context = LocalContext.current

    var isLiked by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
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
                style = MaterialTheme.typography.labelSmall
            )
        }

        @Suppress("MagicNumber")
        val size by animateDpAsState(
            targetValue = if (isLiked) 26.dp else 24.dp,
            animationSpec = keyframes {
                durationMillis = 250
                24.dp at 0 with LinearOutSlowInEasing
                26.dp at 15 with FastOutLinearInEasing
                30.dp at 75
                28.dp at 150
            }
        )

        IconToggleButton(checked = isLiked, onCheckedChange = { isLiked = !isLiked }) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                modifier = Modifier.size(size),
                contentDescription = null,
            )
        }

        IconButton(onClick = { onSoundShareButtonPressed(sound, context) }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun SoundListSnackbar(
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
private fun SoundSectionTabBar(
    categories: List<SoundCategory>,
    selectedTabIndex: Int,
    onCategorySelected: (category: SoundCategory) -> Unit,
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
