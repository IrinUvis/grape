package uvis.irin.grape.soundlist.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import uvis.irin.grape.core.constants.bigPadding
import uvis.irin.grape.core.constants.smallPadding
import uvis.irin.grape.core.ui.components.GrapeSnackbar
import uvis.irin.grape.core.ui.helpers.getString
import uvis.irin.grape.soundlist.ui.components.SoundListLoadingContent
import uvis.irin.grape.soundlist.ui.components.SoundListLoadingErrorContent
import uvis.irin.grape.soundlist.ui.components.SoundListTopAppBar
import uvis.irin.grape.soundlist.ui.components.SoundListLoadedContent
import uvis.irin.grape.soundlist.ui.model.UiSound

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundListContent(
    viewState: SoundListViewState,
    onNavigationIconClicked: () -> Unit,
    onDownloadForOfflineIconClicked: () -> Unit,
    onSettingsIconClicked: () -> Unit,
    onSoundButtonClicked: (UiSound) -> Unit,
    onDownloadSoundClicked: (UiSound) -> Unit,
    onFavouriteButtonClicked: (UiSound) -> Unit,
    onShareButtonClicked: (UiSound) -> Unit,
    onRetryButtonClicked: () -> Unit,
    onErrorSnackbarDismissed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SoundListTopAppBar(
                category = viewState.category,
                soundsLoaded = viewState.soundsLoadingState == SoundsLoadingState.Loaded,
                soundsDownloadState = viewState.soundsDownloadState,
                onNavigationIconClicked = onNavigationIconClicked,
                onDownloadForOfflineIconClicked = onDownloadForOfflineIconClicked,
                onSettingsIconClicked = onSettingsIconClicked,
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                GrapeSnackbar(snackbarData = snackbarData)
            }
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier
                .padding(paddingValues)
                .padding(
                    horizontal = smallPadding,
                    vertical = smallPadding,
                ),
            targetState = viewState.soundsLoadingState
        ) { screenState ->
            when (screenState) {
                SoundsLoadingState.Loading -> {
                    SoundListLoadingContent()
                }
                SoundsLoadingState.Loaded -> {
                    LaunchedEffect(viewState.errorMessage) {
                        viewState.errorMessage?.let {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = it.getString(context),
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Long
                                )
                                onErrorSnackbarDismissed()
                            }
                        }
                    }

                    viewState.sounds?.let { sounds ->
                        SoundListLoadedContent(
                            sounds = sounds,
                            scrollBehavior = scrollBehavior,
                            onSoundButtonClicked = onSoundButtonClicked,
                            onDownloadSoundClicked = onDownloadSoundClicked,
                            onFavouriteButtonClicked = onFavouriteButtonClicked,
                            onShareButtonClicked = onShareButtonClicked,
                        )
                    }
                }
                SoundsLoadingState.LoadingError -> {
                    viewState.errorMessage?.let {
                        SoundListLoadingErrorContent(
                            modifier = Modifier.padding(bigPadding),
                            onRetryButtonClicked = onRetryButtonClicked,
                            errorMessage = it.getString(),
                        )
                    }
                }
            }
        }
    }
}
