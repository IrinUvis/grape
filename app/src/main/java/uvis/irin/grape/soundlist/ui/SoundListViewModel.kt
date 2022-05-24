package uvis.irin.grape.soundlist.ui

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.Sound
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

    fun onSoundPressed(sound: Sound, context: Context) {
        if (sound is ResourceSound) {
            val mediaPlayer = MediaPlayer()
            val descriptor = context.assets.openFd("${sound.category.assetsPath}/${sound.relativeAssetPath}")
            mediaPlayer.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )
            descriptor.close()

            mediaPlayer.prepare()
            mediaPlayer.setVolume(1f, 1f)
            mediaPlayer.isLooping = false
            mediaPlayer.start()
        }
    }
}
