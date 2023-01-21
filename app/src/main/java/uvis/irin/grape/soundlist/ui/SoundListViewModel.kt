package uvis.irin.grape.soundlist.ui

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.FileInputStream
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvis.irin.grape.R
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.core.android.service.file.FileSharingService
import uvis.irin.grape.core.android.service.file.FileWritingService
import uvis.irin.grape.core.ui.helpers.UiText
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

    companion object {
        private const val LOADING_DELAY = 1500L
    }

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
            val delayDeferred = async {
                delay(LOADING_DELAY)
            }

            val loadingDeferred = async {
                val path = _viewState.value.category.absolutePath

                when (val result = fetchSoundsForPathUseCase(path)) {
                    is FetchSoundsForPathResult.Success -> SoundListViewState.SoundsLoaded.Active(
                        category = _viewState.value.category,
                        sounds = result.sounds.map { it.toUiSound() }
                    )
                    is FetchSoundsForPathResult.Failure -> {
                        SoundListViewState.LoadingSoundsError(
                            category = _viewState.value.category,
                            errorMessage = when (result) {
                                FetchSoundsForPathResult.Failure.NoNetwork ->
                                    UiText.ResourceText(R.string.networkErrorMessage)
                                FetchSoundsForPathResult.Failure.ExceededFreeTier ->
                                    UiText.ResourceText(R.string.exceededFreeTierErrorMessage)
                                FetchSoundsForPathResult.Failure.Unexpected ->
                                    UiText.ResourceText(R.string.unexpectedErrorMessage)
                                FetchSoundsForPathResult.Failure.Unknown ->
                                    UiText.ResourceText(R.string.unknownErrorMessage)
                            }
                        )
                    }
                }
            }

            val list = awaitAll(delayDeferred, loadingDeferred)

            _viewState.value = list.last() as SoundListViewState
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
                            _viewState.value = SoundListViewState.SoundsLoaded.Error(
                                category = state.category,
                                sounds = state.sounds,
                                errorMessage = when (result) {
                                    FetchByteArrayForPathResult.Failure.NoNetwork ->
                                        UiText.ResourceText(R.string.networkErrorMessage)
                                    FetchByteArrayForPathResult.Failure.ExceededFreeTier ->
                                        UiText.ResourceText(R.string.exceededFreeTierErrorMessage)
                                    FetchByteArrayForPathResult.Failure.TooLargeFile ->
                                        UiText.ResourceText(R.string.tooLargeFileErrorMessage)
                                    FetchByteArrayForPathResult.Failure.Unexpected ->
                                        UiText.ResourceText(R.string.unexpectedErrorMessage)
                                    FetchByteArrayForPathResult.Failure.Unknown ->
                                        UiText.ResourceText(R.string.unknownErrorMessage)
                                }
                            )
                        }
                    }
                }

                fileReadingService.readFile(sound.path)?.let { soundFile ->
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
                            _viewState.value = SoundListViewState.SoundsLoaded.Error(
                                category = state.category,
                                sounds = state.sounds,
                                errorMessage = when (result) {
                                    FetchByteArrayForPathResult.Failure.NoNetwork ->
                                        UiText.ResourceText(R.string.networkErrorMessage)
                                    FetchByteArrayForPathResult.Failure.ExceededFreeTier ->
                                        UiText.ResourceText(R.string.exceededFreeTierErrorMessage)
                                    FetchByteArrayForPathResult.Failure.TooLargeFile ->
                                        UiText.ResourceText(R.string.tooLargeFileErrorMessage)
                                    FetchByteArrayForPathResult.Failure.Unexpected ->
                                        UiText.ResourceText(R.string.unexpectedErrorMessage)
                                    FetchByteArrayForPathResult.Failure.Unknown ->
                                        UiText.ResourceText(R.string.unknownErrorMessage)
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

    fun resetToActiveSoundsLoaded() {
        viewModelScope.launch {
            (_viewState.value as? SoundListViewState.SoundsLoaded)?.let { state ->
                _viewState.value = SoundListViewState.SoundsLoaded.Active(
                    category = state.category,
                    sounds = state.sounds,
                )
            }
        }
    }
}
