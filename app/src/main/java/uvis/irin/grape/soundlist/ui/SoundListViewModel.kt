package uvis.irin.grape.soundlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvis.irin.grape.soundlist.domain.model.result.FetchSoundsForPathResult
import uvis.irin.grape.soundlist.domain.usecase.FetchSoundsForPathUseCase
import uvis.irin.grape.soundlist.ui.model.UiCategory
import uvis.irin.grape.soundlist.ui.model.UiSound
import uvis.irin.grape.soundlist.ui.model.toUiSound

@HiltViewModel
class SoundListViewModel @Inject constructor(
    private val fetchSoundsForPathUseCase: FetchSoundsForPathUseCase,
) : ViewModel() {

    private val category = UiCategory(
        name = "Jail",
        absolutePath = "sounds/01_jail"
    )

    private val _viewState: MutableStateFlow<SoundListViewState> = MutableStateFlow(
        SoundListViewState.Loading(category)
    )
    val viewState: StateFlow<SoundListViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            val path = _viewState.value.category.absolutePath

            _viewState.value = when (val result = fetchSoundsForPathUseCase(path)) {
                is FetchSoundsForPathResult.Success -> SoundListViewState.SoundsLoaded(
                    category = _viewState.value.category,
                    sounds = result.sounds.map { it.toUiSound() }
                )
                is FetchSoundsForPathResult.Failure -> SoundListViewState.LoadingError(
                    category = _viewState.value.category,
                    errorMessage = when (result) {
                        is FetchSoundsForPathResult.Failure.NoNetwork -> "NoNetwork"
                        is FetchSoundsForPathResult.Failure.ExceededFreeTier -> "ExceededFreeTier"
                        is FetchSoundsForPathResult.Failure.Unexpected -> "Unexpected"
                        is FetchSoundsForPathResult.Failure.Unknown -> "Unknown"
                    }
                )
            }
        }
    }

    fun playSound(sound: UiSound) {

    }

    fun toggleFavouriteSound(sound: UiSound) {
        (_viewState.value as? SoundListViewState.SoundsLoaded)?.let { state ->
            val soundIndex = state.sounds.indexOf(sound)
            val newSounds = state.sounds.mapIndexed { index, previousSound ->
                if (index == soundIndex) UiSound(
                    fileName = sound.fileName,
                    isFavourite = !sound.isFavourite,
                ) else previousSound
            }
            _viewState.value= SoundListViewState.SoundsLoaded(
                category = state.category,
                sounds = newSounds
            )
        }
    }

    fun shareSound(sound: UiSound) {

    }
}
