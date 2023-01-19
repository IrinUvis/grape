package uvis.irin.grape.soundlist.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uvis.irin.grape.soundlist.ui.components.LoadingContent
import uvis.irin.grape.soundlist.ui.components.LoadingErrorContent
import uvis.irin.grape.soundlist.ui.components.SoundListTopAppBar
import uvis.irin.grape.soundlist.ui.components.SoundsLoadedContent
import uvis.irin.grape.soundlist.ui.model.UiSound

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundListContent(
    viewState: SoundListViewState,
    onNavigationIconPressed: () -> Unit,
    onSoundButtonClicked: (UiSound) -> Unit,
    onFavouriteButtonClicked: (UiSound) -> Unit,
    onShareButtonClicked: (UiSound) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            SoundListTopAppBar(
                category = viewState.category,
                onNavigationIconPressed = onNavigationIconPressed,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier
                .padding(paddingValues)
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp,
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
                    (viewState as? SoundListViewState.SoundsLoaded)?.let {
                        SoundsLoadedContent(
                            sounds = viewState.sounds,
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
                            errorMessage = viewState.errorMessage,
                        )
                    }
                }
            }
        }
    }
}
