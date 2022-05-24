package uvis.irin.grape.soundlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.usecase.GetAllSoundsByCategoryUseCase
import uvis.irin.grape.soundlist.domain.usecase.GetSoundCategoriesUseCase

class SoundListViewModel(
    private val getSoundCategoriesUseCase: GetSoundCategoriesUseCase,
    private val getAllSoundsByCategoryUseCase: GetAllSoundsByCategoryUseCase
) : ViewModel() {
    private val _viewState: MutableStateFlow<SoundListViewState> =
        MutableStateFlow(SoundListViewState())
    val viewState: StateFlow<SoundListViewState> = _viewState

    init {
        viewModelScope.launch {
            val getSoundCategoriesResult = getSoundCategoriesUseCase.invoke()

            _viewState.value = when (getSoundCategoriesResult) {
                is Result.Success -> {
                    _viewState.value.copy(
                        showLoading = false,
                        categories = getSoundCategoriesResult.data
                    )
                }
                is Result.Error -> {
                    _viewState.value.copy(
                        showLoading = false,
                        categories = emptyList()
                    )
                }
            }

            val getAllSoundsByCategoryResult = getAllSoundsByCategoryUseCase.invoke(
                _viewState.value.categories.first()
            )

            _viewState.value = when (getAllSoundsByCategoryResult) {
                is Result.Success -> {
                    _viewState.value.copy(
                        sounds = getAllSoundsByCategoryResult.data
                    )
                }
                is Result.Error -> {
                    _viewState.value.copy(
                        sounds = emptyList()
                    )
                }
            }
        }
    }
}
