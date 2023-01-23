package uvis.irin.grape.soundlist.ui

import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.core.ui.helpers.UiText
import uvis.irin.grape.soundlist.ui.model.DownloadState
import uvis.irin.grape.soundlist.ui.model.UiSound

data class SoundListViewState(
    val category: UiCategory,
    val soundsLoadingState: SoundsLoadingState,
    val sounds: List<UiSound>?,
    val soundsDownloadState: DownloadState,
    val errorMessage: UiText?,
)

enum class SoundsLoadingState {
    Loading,
    Loaded,
    LoadingError,
}
