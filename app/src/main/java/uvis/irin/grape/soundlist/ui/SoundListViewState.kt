package uvis.irin.grape.soundlist.ui

import uvis.irin.grape.core.ui.helpers.UiText
import uvis.irin.grape.soundlist.ui.model.DownloadState
import uvis.irin.grape.soundlist.ui.model.UiCategory
import uvis.irin.grape.soundlist.ui.model.UiSound

sealed class SoundListViewState(
    open val category: UiCategory,
    val type: SoundListViewStateType,
) {

    data class LoadingSounds(
        override val category: UiCategory,
    ) : SoundListViewState(
        category = category,
        type = SoundListViewStateType.Loading
    )

    sealed class SoundsLoaded(
        override val category: UiCategory,
        open val sounds: List<UiSound>,
        open val soundsDownloadState: DownloadState,
    ) : SoundListViewState(
        category = category,
        type = SoundListViewStateType.SoundsLoaded
    ) {
        data class Active(
            override val category: UiCategory,
            override val sounds: List<UiSound>,
            override val soundsDownloadState: DownloadState,
        ) : SoundsLoaded(
            category = category,
            sounds = sounds,
            soundsDownloadState = soundsDownloadState,
        )

        data class Error(
            override val category: UiCategory,
            override val sounds: List<UiSound>,
            override val soundsDownloadState: DownloadState,
            val errorMessage: UiText,
        ) : SoundsLoaded(
            category = category,
            sounds = sounds,
            soundsDownloadState = soundsDownloadState,
        )
    }

    data class LoadingSoundsError(
        override val category: UiCategory,
        val errorMessage: UiText,
    ) : SoundListViewState(
        category = category,
        type = SoundListViewStateType.LoadingSoundsError
    )
}

enum class SoundListViewStateType {
    Loading,
    SoundsLoaded,
    LoadingSoundsError,
}
