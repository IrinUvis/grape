package uvis.irin.grape.soundlist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SoundListScreen(
    viewModel: SoundListViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
) {
    val viewState = viewModel.viewState.collectAsState()

    SoundListContent(
        viewState = viewState.value,
        onNavigationIconClicked = navigateUp,
        onDownloadForOfflineIconClicked = viewModel::downloadOrRemoveAllSounds,
        onClearSearchQueryClicked = viewModel::clearSearchQuery,
        onSearchIconClicked = viewModel::toggleSearchInput,
        onSearchQueryChanged = viewModel::changeSearchQuery,
        onShowOnlyFavouritesClicked = viewModel::toggleShowOnlyFavourites,
        onDownloadSoundClicked = viewModel::downloadOrRemoveSound,
        onSoundButtonClicked = viewModel::playSound,
        onFavouriteButtonClicked = viewModel::toggleFavouriteSound,
        onShareButtonClicked = viewModel::shareSound,
        onRetryButtonClicked = viewModel::retryLoadingSounds,
        onErrorSnackbarDismissed = viewModel::clearErrorMessage
    )
}
