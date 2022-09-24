package uvis.irin.grape.soundlist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SoundListScreen(
    viewModel: SoundListViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()

    SoundListContent(
        viewState = viewState.value,
        onSoundPressed = viewModel::onSoundPressed,
        onSoundShareButtonPressed = viewModel::onSoundShareButtonPressed,
        onCategorySelected = viewModel::onCategorySelected,
        onSubcategorySelected = viewModel::onSubcategorySelected,
        onFavouriteButtonPressed = viewModel::onFavouriteButtonPressed,
        onBackButtonPressed = viewModel::onBackButtonPressed,
        onErrorSnackbarDismissed = viewModel::onErrorSnackbarDismissed
    )
}
