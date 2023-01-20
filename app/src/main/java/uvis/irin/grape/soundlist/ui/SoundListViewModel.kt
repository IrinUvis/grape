package uvis.irin.grape.soundlist.ui

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.FileInputStream
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.core.android.service.file.FileSharingService
import uvis.irin.grape.core.android.service.file.FileWritingService
import uvis.irin.grape.soundlist.domain.model.result.FetchByteArrayForPathResult
import uvis.irin.grape.soundlist.domain.model.result.FetchSoundsForPathResult
import uvis.irin.grape.soundlist.domain.usecase.FetchByteArrayForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchSoundsForPathUseCase
import uvis.irin.grape.soundlist.ui.model.UiCategory
import uvis.irin.grape.soundlist.ui.model.UiSound
import uvis.irin.grape.soundlist.ui.model.toUiSound

@HiltViewModel
class SoundListViewModel @Inject constructor(
    private val fetchSoundsForPathUseCase: FetchSoundsForPathUseCase,
    private val fetchByteArrayForPathUseCase: FetchByteArrayForPathUseCase,
    private val fileSharingService: FileSharingService,
    private val fileWritingService: FileWritingService,
    private val fileReadingService: FileReadingService,
) : ViewModel() {

    private val mediaPlayer = MediaPlayer()

    private val category = UiCategory(
        name = "Jail",
        absolutePath = "sounds/01_jail"
    ) // TOA did sth cool with navigation args

    private val _viewState: MutableStateFlow<SoundListViewState> = MutableStateFlow(
        SoundListViewState.LoadingSounds(category)
    )
    val viewState: StateFlow<SoundListViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            val path = _viewState.value.category.absolutePath

            _viewState.value = when (val result = fetchSoundsForPathUseCase(path)) {
                is FetchSoundsForPathResult.Success -> SoundListViewState.SoundsLoaded.Active(
                    category = _viewState.value.category,
                    sounds = result.sounds.map { it.toUiSound() }
                )
                is FetchSoundsForPathResult.Failure -> SoundListViewState.LoadingSoundsError(
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
        viewModelScope.launch {
            (_viewState.value as? SoundListViewState.SoundsLoaded)?.let { state ->
                val loadedFile = fileReadingService.readFile(sound.path)

                if (loadedFile == null) {
                    when (val result = fetchByteArrayForPathUseCase(sound.path)) {
                        is FetchByteArrayForPathResult.Success -> {
                            val bytes = result.bytes

                            fileWritingService.writeFile(sound.path, bytes)
                        }
                        is FetchByteArrayForPathResult.Failure -> {
                            _viewState.value = SoundListViewState.SoundsLoaded.DownloadingError(
                                category = _viewState.value.category,
                                sounds = state.sounds,
                                errorMessage = when (result) {
                                    is FetchByteArrayForPathResult.Failure.NoNetwork -> "NoNetwork"
                                    is FetchByteArrayForPathResult.Failure.ExceededFreeTier -> "ExceededFreeTier"
                                    is FetchByteArrayForPathResult.Failure.TooLargeFile -> "TooLargeFile"
                                    is FetchByteArrayForPathResult.Failure.Unexpected -> "Unexpected"
                                    is FetchByteArrayForPathResult.Failure.Unknown -> "Unknown"
                                }
                            )
                        }
                    }
                }

                fileReadingService.readFile(sound.path)?.let {soundFile ->
                    mediaPlayer.reset()
                    val fis = FileInputStream(soundFile)
                    mediaPlayer.setDataSource(fis.fd)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                }
            }
        }
    }

    fun toggleFavouriteSound(sound: UiSound) {
        (_viewState.value as? SoundListViewState.SoundsLoaded)?.let { state ->
            val soundIndex = state.sounds.indexOf(sound)
            val newSounds = state.sounds.mapIndexed { index, previousSound ->
                if (index == soundIndex) UiSound(
                    fileName = sound.fileName,
                    path = sound.path,
                    isFavourite = !sound.isFavourite,
                ) else previousSound
            }
            _viewState.value = SoundListViewState.SoundsLoaded.Active(
                category = state.category,
                sounds = newSounds
            )
        }
    }

    fun shareSound(sound: UiSound) {
        viewModelScope.launch {
            (_viewState.value as? SoundListViewState.SoundsLoaded)?.let { state ->
                val loadedFile = fileReadingService.readFile(sound.path)

                if (loadedFile == null) {
                    when (val result = fetchByteArrayForPathUseCase(sound.path)) {
                        is FetchByteArrayForPathResult.Success -> {
                            val bytes = result.bytes

                            fileWritingService.writeFile(sound.path, bytes)
                        }
                        is FetchByteArrayForPathResult.Failure -> {
                            _viewState.value = SoundListViewState.SoundsLoaded.DownloadingError(
                                category = _viewState.value.category,
                                sounds = state.sounds,
                                errorMessage = when (result) {
                                    is FetchByteArrayForPathResult.Failure.NoNetwork -> "NoNetwork"
                                    is FetchByteArrayForPathResult.Failure.ExceededFreeTier -> "ExceededFreeTier"
                                    is FetchByteArrayForPathResult.Failure.TooLargeFile -> "TooLargeFile"
                                    is FetchByteArrayForPathResult.Failure.Unexpected -> "Unexpected"
                                    is FetchByteArrayForPathResult.Failure.Unknown -> "Unknown"
                                }
                            )
                        }
                    }
                }

                fileReadingService.readFile(sound.path)?.let { soundFile ->
                    fileSharingService.shareFile(soundFile, mimeType = "audio/mp3")
                }
            }
        }
    }
}
