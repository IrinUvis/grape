package uvis.irin.grape.soundlist.ui

import android.media.MediaPlayer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uvis.irin.grape.R
import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.core.android.service.file.FileDeletingService
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.core.android.service.file.FileSharingService
import uvis.irin.grape.core.android.service.file.FileWritingService
import uvis.irin.grape.core.extension.withDashesReplacedByForwardSlashes
import uvis.irin.grape.core.extension.withItemAtIndex
import uvis.irin.grape.core.ui.helpers.UiText
import uvis.irin.grape.navigation.SOUND_LIST_ARG
import uvis.irin.grape.soundlist.domain.model.result.FetchByteArrayForPathResult
import uvis.irin.grape.soundlist.domain.model.result.FetchSoundsForPathResult
import uvis.irin.grape.soundlist.domain.usecase.FetchByteArrayForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchSoundsForPathUseCase
import uvis.irin.grape.soundlist.ui.model.DownloadState
import uvis.irin.grape.soundlist.ui.model.UiSound
import uvis.irin.grape.soundlist.ui.model.toUiSound

@HiltViewModel
class SoundListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fetchSoundsForPathUseCase: FetchSoundsForPathUseCase,
    private val fetchByteArrayForPathUseCase: FetchByteArrayForPathUseCase,
    private val fileSharingService: FileSharingService,
    private val fileWritingService: FileWritingService,
    private val fileReadingService: FileReadingService,
    private val fileDeletingService: FileDeletingService,
) : ViewModel() {

    companion object {
        private const val MP3_MIME_TYPE = "audio/mp3"
    }

    private val mediaPlayer = MediaPlayer()

    private val categoryPath = (checkNotNull(savedStateHandle[SOUND_LIST_ARG]) as String)
        .withDashesReplacedByForwardSlashes()

    private val _viewState: MutableStateFlow<SoundListViewState> = MutableStateFlow(
        SoundListViewState(
            category = UiCategory(
                path = categoryPath,
                isFirstCategory = false,
                isFinalCategory = true,
                bitmap = null,
            ),
            soundsLoadingState = SoundsLoadingState.Loading,
            sounds = null,
            soundsDownloadState = DownloadState.NotDownloaded,
            errorMessage = null
        )
    )
    val viewState: StateFlow<SoundListViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            loadSounds()
        }
    }

    fun downloadOrRemoveSound(sound: UiSound) {
        viewModelScope.launch {
            if (sound.downloadState == DownloadState.NotDownloaded ||
                sound.downloadState == DownloadState.ErrorDownloading
            ) {
                downloadSoundAndSaveIt(sound)
            } else {
                removeSoundFile(sound)
            }

            _viewState.update {
                viewStateWithUpdatedSoundsDownloadState()
            }
        }
    }

    fun downloadOrRemoveAllSounds() {
        viewModelScope.launch {
            if (_viewState.value.soundsDownloadState == DownloadState.NotDownloaded) {
                _viewState.update {
                    it.copy(
                        soundsDownloadState = DownloadState.Downloading,
                    )
                }

                _viewState.value.sounds?.let { sounds ->
                    val nonDownloadedSounds =
                        sounds.filter { !fileReadingService.fileExists(it.path) }

                    nonDownloadedSounds.map { downloadSoundAndSaveIt(it) }
                }

                _viewState.update {
                    viewStateWithUpdatedSoundsDownloadState()
                }
            } else {
                _viewState.value.sounds?.let { sounds ->
                    val downloadedSounds = sounds.filter { fileReadingService.fileExists(it.path) }

                    downloadedSounds.map { removeSoundFile(it) }
                }

                _viewState.update {
                    viewStateWithUpdatedSoundsDownloadState()
                }
            }
        }
    }

    fun playSound(sound: UiSound) {
        viewModelScope.launch {
            var fileToPlay = sound.localFile
            if (fileToPlay == null) {
                when (val result = fetchByteArrayForPathUseCase(sound.path)) {
                    is FetchByteArrayForPathResult.Success -> {
                        fileToPlay = clearCacheAndCreateCachedFile(sound, result.bytes)
                    }
                    is FetchByteArrayForPathResult.Failure -> {
                        _viewState.update {
                            viewStateForFetchByteArrayForPathFailure(
                                result = result,
                            )
                        }
                    }
                }
            }

            fileToPlay?.let { playViaMediaPlayer(fileToPlay) }
        }
    }

    fun toggleFavouriteSound(sound: UiSound) {
        _viewState.value.sounds?.let { sounds ->
            val soundIndex = sounds.indexOf(sound)
            val newSound = sound.copy(
                isFavourite = !sound.isFavourite
            )

            _viewState.update {
                it.copy(
                    sounds = sounds.withItemAtIndex(newSound, soundIndex),
                )
            }
        }
    }

    fun shareSound(sound: UiSound) {
        viewModelScope.launch {
            var fileToShare = sound.localFile
            if (fileToShare == null) {
                when (val result = fetchByteArrayForPathUseCase(sound.path)) {
                    is FetchByteArrayForPathResult.Success -> {
                        fileToShare = clearCacheAndCreateCachedFile(sound, result.bytes)
                    }
                    is FetchByteArrayForPathResult.Failure -> {
                        _viewState.update {
                            viewStateForFetchByteArrayForPathFailure(
                                result = result,
                            )
                        }
                    }
                }
            }

            fileToShare?.let {
                fileSharingService.shareFile(
                    fileToShare,
                    mimeType = MP3_MIME_TYPE
                )
            }
        }
    }

    fun retryLoadingSounds() {
        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    soundsLoadingState = SoundsLoadingState.Loading,
                    errorMessage = null
                )
            }

            loadSounds()
        }
    }

    fun clearErrorMessage() {
        _viewState.update {
            it.copy(
                errorMessage = null
            )
        }
    }

    private suspend fun loadSounds() {
        val path = _viewState.value.category.path

        val newViewState = when (val result = fetchSoundsForPathUseCase(path)) {
            is FetchSoundsForPathResult.Success -> {
                val sounds = soundsForFetchSoundsForPathSuccess(result)

                deleteSoundsNotPresentInTheCloudFromInternalStorage(cloudSounds = sounds)

                viewStateForInitiallyLoadedSounds(sounds)
            }
            is FetchSoundsForPathResult.Failure -> {
                val offlineSounds = readSoundsAvailableOffline()

                if (offlineSounds.isEmpty()) {
                    viewStateForFetchSoundsForPathFailure(
                        result = result
                    )
                } else {
                    val sounds = offlineSounds.map { it.toUiSound() }

                    viewStateForInitiallyLoadedSounds(sounds)
                }
            }
        }

        _viewState.update {
            newViewState
        }
    }

    private suspend fun readSoundsAvailableOffline(): List<File> {
        val path = _viewState.value.category.path

        val allFiles = fileReadingService.readAllFilesInDirectory(path)

        return allFiles.filter { it.extension == "mp3" }
    }

    private suspend fun downloadSoundAndSaveIt(sound: UiSound) {
        val downloadingSound = sound.copy(
            downloadState = DownloadState.Downloading,
        )
        _viewState.value.sounds?.let { sounds ->
            val soundIndex = sounds.indexOf(sound)

            _viewState.update {
                it.copy(
                    sounds = sounds.withItemAtIndex(downloadingSound, soundIndex),
                )
            }
        }

        when (val result = fetchByteArrayForPathUseCase(sound.path)) {
            is FetchByteArrayForPathResult.Success -> {
                val file =
                    fileWritingService.writeFileToInternalStorage(sound.path, result.bytes)

                _viewState.value.sounds?.let { sounds ->
                    val soundIndex = sounds.indexOf(downloadingSound)
                    val downloadedSound = sound.copy(
                        downloadState = DownloadState.Downloaded,
                        localFile = file
                    )

                    _viewState.update {
                        it.copy(
                            sounds = sounds.withItemAtIndex(
                                downloadedSound,
                                soundIndex
                            )
                        )
                    }
                }
            }
            is FetchByteArrayForPathResult.Failure -> {
                _viewState.value.sounds?.let { sounds ->
                    val soundIndex = sounds.indexOf(downloadingSound)
                    val soundWithErrorDownloading = sound.copy(
                        downloadState = DownloadState.ErrorDownloading
                    )

                    _viewState.update {
                        it.copy(
                            sounds = sounds.withItemAtIndex(
                                soundWithErrorDownloading,
                                soundIndex
                            ),
                            errorMessage = errorMessageForFetchByteArrayForPathFailure(result),
                        )
                    }
                }
            }
        }
    }

    private suspend fun removeSoundFile(sound: UiSound) {
        val file = fileReadingService.readFile(sound.path)

        if (file != null) {
            fileDeletingService.deleteFile(file)
        }

        _viewState.value.sounds?.let { sounds ->
            val soundIndex = sounds.indexOf(sound)
            val notDownloadedSound = sound.copy(
                downloadState = DownloadState.NotDownloaded,
                localFile = null,
            )

            _viewState.update {
                it.copy(
                    sounds = sounds.withItemAtIndex(notDownloadedSound, soundIndex),
                )
            }
        }
    }

    private fun playViaMediaPlayer(file: File) {
        mediaPlayer.reset()
        val fis = FileInputStream(file)
        mediaPlayer.setDataSource(fis.fd)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    private suspend fun deleteSoundsNotPresentInTheCloudFromInternalStorage(cloudSounds: List<UiSound>) {
        val path = _viewState.value.category.path

        val cloudSoundsNames = cloudSounds.map { it.fileName }

        val files =
            fileReadingService.readAllFilesInDirectory(path)

        val filesToDelete = files.filter { !cloudSoundsNames.contains(it.name) }

        for (fileToDelete in filesToDelete) {
            fileDeletingService.deleteFile(fileToDelete)
        }
    }

    private suspend fun clearCacheAndCreateCachedFile(sound: UiSound, bytes: ByteArray): File {
        fileDeletingService.clearCache()

        return fileWritingService.writeFileToCachedStorage(sound.fileName, bytes)
    }

    private fun viewStateWithUpdatedSoundsDownloadState(): SoundListViewState {
        return _viewState.value.sounds?.let { sounds ->
            _viewState.value.copy(
                soundsDownloadState = soundsDownloadStateForSounds(sounds),
            )
        } ?: _viewState.value
    }

    private fun soundsDownloadStateForSounds(sounds: List<UiSound>): DownloadState {
        val allDownloaded = sounds.all { it.downloadState == DownloadState.Downloaded }

        return if (allDownloaded) {
            DownloadState.Downloaded
        } else {
            DownloadState.NotDownloaded
        }
    }

    private suspend fun soundsForFetchSoundsForPathSuccess(
        result: FetchSoundsForPathResult.Success
    ): List<UiSound> {
        return result.sounds.map { it.toUiSound() }.map {
            val file = fileReadingService.readFile(it.path)
            val fileDownloaded = file != null
            val downloadState =
                if (fileDownloaded) DownloadState.Downloaded else DownloadState.NotDownloaded
            it.copy(
                downloadState = downloadState,
                localFile = file,
            )
        }
    }

    private fun viewStateForInitiallyLoadedSounds(sounds: List<UiSound>): SoundListViewState {
        val allDownloaded = sounds.none { it.downloadState == DownloadState.NotDownloaded }
        val soundsDownloadState =
            if (allDownloaded) DownloadState.Downloaded else DownloadState.NotDownloaded

        return _viewState.value.copy(
            sounds = sounds,
            soundsLoadingState = SoundsLoadingState.Loaded,
            soundsDownloadState = soundsDownloadState,
        )
    }

    private fun viewStateForFetchSoundsForPathFailure(
        result: FetchSoundsForPathResult.Failure
    ): SoundListViewState {
        return _viewState.value.copy(
            soundsLoadingState = SoundsLoadingState.LoadingError,
            errorMessage = errorMessageForFetchSoundsForPathFailure(result)
        )
    }

    private fun errorMessageForFetchSoundsForPathFailure(
        result: FetchSoundsForPathResult.Failure,
    ): UiText {
        return when (result) {
            FetchSoundsForPathResult.Failure.NoNetwork ->
                UiText.ResourceText(R.string.networkAndNoDownloadedSoundsErrorMessage)
            FetchSoundsForPathResult.Failure.ExceededFreeTier ->
                UiText.ResourceText(R.string.exceededFreeTierAndNoDownloadedSoundsErrorMessage)
            FetchSoundsForPathResult.Failure.Unexpected ->
                UiText.ResourceText(R.string.unexpectedAndNoDownloadedSoundsErrorMessage)
            FetchSoundsForPathResult.Failure.Unknown ->
                UiText.ResourceText(R.string.unknownAndNoDownloadedSoundsErrorMessage)
        }
    }

    private fun viewStateForFetchByteArrayForPathFailure(
        result: FetchByteArrayForPathResult.Failure,
    ): SoundListViewState {
        return _viewState.value.copy(
            soundsLoadingState = SoundsLoadingState.Loaded,
            errorMessage = errorMessageForFetchByteArrayForPathFailure(result)
        )
    }

    private fun errorMessageForFetchByteArrayForPathFailure(
        result: FetchByteArrayForPathResult.Failure,
    ): UiText {
        return when (result) {
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
    }
}
