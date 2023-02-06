package uvis.irin.grape.soundlist.ui

import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.core.ui.helpers.UiText
import uvis.irin.grape.soundlist.ui.model.DownloadState
import uvis.irin.grape.soundlist.ui.model.UiSound

data class SoundListViewState(
    val category: UiCategory,
    val soundsLoadingState: SoundsLoadingState = SoundsLoadingState.Loading,
    val searchQuery: UiText = UiText.StringText(""),
    val sounds: List<UiSound>? = null,
    val soundsDownloadState: DownloadState = DownloadState.NotDownloaded,
    val isSearchExpanded: Boolean = false,
    val errorMessage: UiText? = null,
)

enum class SoundsLoadingState {
    Loading,
    Loaded,
    LoadingError,
}
