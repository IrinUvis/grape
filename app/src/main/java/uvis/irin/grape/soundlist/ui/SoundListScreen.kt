package uvis.irin.grape.soundlist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SoundListScreen(
    viewModel: SoundListViewModel = hiltViewModel(),
) {
    val viewState = viewModel.viewState.collectAsState()

    SoundListContent(
        viewState = viewState.value,
        onNavigationIconClicked = { },
        onDownloadForOfflineIconClicked = viewModel::downloadOrRemoveAllSounds,
        onSettingsIconClicked = { },
        onDownloadSoundClicked = viewModel::downloadOrRemoveSound,
        onSoundButtonClicked = viewModel::playSound,
        onFavouriteButtonClicked = viewModel::toggleFavouriteSound,
        onShareButtonClicked = viewModel::shareSound,
        onErrorSnackbarDismissed = viewModel::resetToActiveSoundsLoaded
    )
}
