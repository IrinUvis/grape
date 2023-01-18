package uvis.irin.grape.soundlist.ui

import uvis.irin.grape.soundlist.ui.model.UiCategory
import uvis.irin.grape.soundlist.ui.model.UiSound

sealed class SoundListViewState(
    open val category: UiCategory,
    val type: SoundListViewStateType,
) {

    data class Loading(
        override val category: UiCategory,
    ) : SoundListViewState(
        category = category,
        type = SoundListViewStateType.Loading
    )

    data class SoundsLoaded(
        override val category: UiCategory,
        val sounds: List<UiSound>,
    ) : SoundListViewState(
        category = category,
        type = SoundListViewStateType.SoundsLoaded
    )

    data class LoadingError(
        override val category: UiCategory,
        val errorMessage: String,
    ) : SoundListViewState(
        category = category,
        type = SoundListViewStateType.LoadingError
    )
}

enum class SoundListViewStateType {
    Loading,
    SoundsLoaded,
    LoadingError,
}