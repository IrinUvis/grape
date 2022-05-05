package uvis.irin.grape.soundlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.SoundCategory
import uvis.irin.grape.soundlist.domain.usecase.GetSoundCategoriesUseCase

class SoundListViewModel(
    private val getSoundCategoriesUseCase: GetSoundCategoriesUseCase,
) : ViewModel() {
    private val _viewState: MutableStateFlow<SoundListViewState> =
        MutableStateFlow(SoundListViewState())
    val viewState: StateFlow<SoundListViewState> = _viewState

    init {
        viewModelScope.launch {
            val getSoundCategoriesResult = getSoundCategoriesUseCase.invoke()

            _viewState.value = when(getSoundCategoriesResult) {
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
        }
    }

}