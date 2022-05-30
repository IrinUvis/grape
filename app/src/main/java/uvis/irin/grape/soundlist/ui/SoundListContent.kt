@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class, ExperimentalPagerApi::class
)

package uvis.irin.grape.soundlist.ui

import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch
import uvis.irin.grape.core.ui.components.GrapeButton
import uvis.irin.grape.core.ui.theme.GrapeTheme
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory

@Composable
fun SoundListContent(
    viewState: SoundListViewState,
    onSoundPressed: (sound: Sound, context: Context) -> Unit,
    onSoundLongPressed: (sound: Sound, context: Context) -> Unit,
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
                    onSoundLongPressed = onSoundLongPressed,
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
    onSoundLongPressed: (sound: Sound, context: Context) -> Unit,
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
                    .navigationBarsHeight()
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
                GrapeButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSoundPressed(sound, context) },
                    onLongClick = { onSoundLongPressed(sound, context) }
                ) {
                    Text(text = sound.name)
                }
            }
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
            rememberInsetsPaddingValues(
                LocalWindowInsets.current.statusBars,
                applyBottom = false,
            ),
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
            onSoundLongPressed = { _, _ -> },
            onCategorySelected = { },
            onBackButtonPressed = { },
            onErrorSnackbarDismissed = { }
        )
    }
}
