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
import uvis.irin.grape.core.constants.smallPadding
import uvis.irin.grape.core.ui.helpers.getString
import uvis.irin.grape.soundlist.ui.components.LoadingContent
import uvis.irin.grape.soundlist.ui.components.LoadingErrorContent
import uvis.irin.grape.soundlist.ui.components.SoundListTopAppBar
import uvis.irin.grape.soundlist.ui.components.SoundsLoadedActiveContent
import uvis.irin.grape.soundlist.ui.model.UiSound

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundListContent(
    viewState: SoundListViewState,
    onNavigationIconPressed: () -> Unit,
    onSoundButtonClicked: (UiSound) -> Unit,
    onFavouriteButtonClicked: (UiSound) -> Unit,
    onShareButtonClicked: (UiSound) -> Unit,
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
                onNavigationIconPressed = onNavigationIconPressed,
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier
                .padding(paddingValues)
                .padding(
                    horizontal = smallPadding,
                    vertical = smallPadding,
                ),
            targetState = viewState.type
        ) { stateType ->
            when (stateType) {
                SoundListViewStateType.Loading -> {
                    (viewState as? SoundListViewState.LoadingSounds)?.let {
                        LoadingContent()
                    }
                }
                SoundListViewStateType.SoundsLoaded -> {
                    (viewState as? SoundListViewState.SoundsLoaded)?.let { state ->
                        LaunchedEffect(state) {
                            if (state is SoundListViewState.SoundsLoaded.Error) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = state.errorMessage.getString(context),
                                        duration = SnackbarDuration.Short
                                    )
                                    onErrorSnackbarDismissed()
                                }
                            }
                        }

                        SoundsLoadedActiveContent(
                            sounds = state.sounds,
                            scrollBehavior = scrollBehavior,
                            onSoundButtonClicked = onSoundButtonClicked,
                            onFavouriteButtonClicked = onFavouriteButtonClicked,
                            onShareButtonClicked = onShareButtonClicked,
                        )
                    }
                }
                SoundListViewStateType.LoadingSoundsError -> {
                    (viewState as? SoundListViewState.LoadingSoundsError)?.let {
                        LoadingErrorContent(
                            errorMessage = "sth",
                        )
                    }
                }
            }
        }
    }
}
