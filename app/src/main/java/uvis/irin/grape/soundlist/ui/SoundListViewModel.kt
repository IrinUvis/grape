package uvis.irin.grape.soundlist.ui

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvis.irin.grape.core.provider.file.FileSharingService
import uvis.irin.grape.soundlist.domain.model.result.FetchByteArrayForPathResult
import uvis.irin.grape.soundlist.domain.model.result.FetchSoundsForPathResult
import uvis.irin.grape.soundlist.domain.usecase.FetchByteArrayForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchSoundsForPathUseCase
import uvis.irin.grape.soundlist.ui.model.UiCategory
import uvis.irin.grape.soundlist.ui.model.UiSound
import uvis.irin.grape.soundlist.ui.model.toUiSound

@HiltViewModel
class SoundListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fetchSoundsForPathUseCase: FetchSoundsForPathUseCase,
    private val fetchByteArrayForPathUseCase: FetchByteArrayForPathUseCase,
    private val fileSharingService: FileSharingService,
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
                when (val result = fetchByteArrayForPathUseCase(sound.path)) {
                    is FetchByteArrayForPathResult.Success -> {
                        val bytes = result.bytes

                        // Creating file from downloaded bytes and save it in cache
                        val outputDir = context.cacheDir
                        val soundFile = File.createTempFile("sound", ".mp3", outputDir)
                        val fos = FileOutputStream(soundFile)
                        fos.write(bytes)
                        fos.close()

                        // Play the sound
                        mediaPlayer.reset()
                        val fis = FileInputStream(soundFile)
                        mediaPlayer.setDataSource(fis.fd)
                        mediaPlayer.prepare()
                        mediaPlayer.start()

                        // Delete the sound from cache
                        soundFile.delete()
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
                when (val result = fetchByteArrayForPathUseCase(sound.path)) {
                    is FetchByteArrayForPathResult.Success -> {
                        val bytes = result.bytes

                        // Creating file from downloaded bytes and save it in cache
                        val outputDir = context.cacheDir
                        val soundFile = File.createTempFile("sound", ".mp3", outputDir)
                        val fos = FileOutputStream(soundFile)
                        fos.write(bytes)
                        fos.close()

                        // Share the file
                        fileSharingService.shareFile(soundFile, mimeType = "audio/mp3")

                        // Don't delete the file from cache as it is required by the content receivers of other apps.
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
        }
    }
}
