@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class, ExperimentalPagerApi::class
)

package uvis.irin.grape.soundlist.ui

import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch
//import uvis.irin.grape.core.ui.components.GrapeButton
import uvis.irin.grape.core.ui.theme.GrapeTheme
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory

@Composable
fun SoundListContent(
    viewState: SoundListViewState,
    onSoundPressed: (sound: Sound, context: Context) -> Unit,
    onSoundShareButtonPressed: (sound: Sound, context: Context) -> Unit,
    onCategorySelected: (category: SoundCategory) -> Unit,
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
                    onBackButtonPressed = onBackButtonPressed,
                    onErrorSnackbarDismissed = onErrorSnackbarDismissed
                )
            }
        }
    }
}

@Composable
fun LoadedSoundListContent(
    viewState: SoundListViewState,
    onSoundPressed: (sound: Sound, context: Context) -> Unit,
    onSoundShareButtonPressed: (sound: Sound, context: Context) -> Unit,
    onCategorySelected: (category: SoundCategory) -> Unit,
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
            SoundSectionTabBar(
                categories = viewState.categories,
                selectedTabIndex = viewState.categories.indexOf(viewState.selectedCategory),
                onCategorySelected = onCategorySelected
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
                style = MaterialTheme.typography.titleMedium
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
    onCategorySelected: (category: SoundCategory) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = { },
        edgePadding = 8.dp,
        modifier = Modifier.padding(
            WindowInsets.statusBars.asPaddingValues()
        ),
    ) {
        categories.forEachIndexed { index, category ->
            SoundSectionTab(
                text = category.name,
                selected = selectedTabIndex == index,
                onClick = { onCategorySelected(category) },
            )
        }
    }
}

@Composable
private fun SoundSectionTab(text: String, selected: Boolean, onClick: () -> Unit) {
    val cardColors = if (selected) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    } else {
        CardDefaults.cardColors()
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp),
        colors = cardColors,
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
        )
    }
}

@Preview(
    name = "Night Mode - Empty",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day Mode - Empty",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Suppress("UnusedPrivateMember")
@Composable
private fun SoundListContentPreview() {
    GrapeTheme {
        LoadedSoundListContent(
            viewState = SoundListViewState(),
            onSoundPressed = { _, _ -> },
            onSoundShareButtonPressed = { _, _ -> },
            onCategorySelected = { },
            onBackButtonPressed = { },
            onErrorSnackbarDismissed = { }
        )
    }
}
