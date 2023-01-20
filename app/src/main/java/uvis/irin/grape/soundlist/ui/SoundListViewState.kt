package uvis.irin.grape.soundlist.ui

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
    ) : SoundListViewState(
        category = category,
        type = SoundListViewStateType.SoundsLoaded
    ) {
        data class Active(
            override val category: UiCategory,
            override val sounds: List<UiSound>,
        ) : SoundsLoaded(
            category = category,
            sounds = sounds,
        )

        data class DownloadingError(
            override val category: UiCategory,
            override val sounds: List<UiSound>,
            val errorMessage: String,
        ) : SoundsLoaded(
            category = category,
            sounds = sounds,
        )
    }


    data class LoadingSoundsError(
        override val category: UiCategory,
        val errorMessage: String,
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
