package uvis.irin.grape.soundlist.ui

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvis.irin.grape.R
import uvis.irin.grape.core.android.service.file.FileDeletingService
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.core.android.service.file.FileSharingService
import uvis.irin.grape.core.android.service.file.FileWritingService
import uvis.irin.grape.core.extension.withItemAtIndex
import uvis.irin.grape.core.ui.helpers.UiText
import uvis.irin.grape.soundlist.domain.model.result.FetchByteArrayForPathResult
import uvis.irin.grape.soundlist.domain.model.result.FetchSoundsForPathResult
import uvis.irin.grape.soundlist.domain.usecase.FetchByteArrayForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchSoundsForPathUseCase
import uvis.irin.grape.soundlist.ui.model.DownloadState
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
    private val fileDeletingService: FileDeletingService,
) : ViewModel() {

    companion object {
        private const val MP3_MIME_TYPE = "audio/mp3"
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
            val loadingDeferred = async {
                val path = _viewState.value.category.absolutePath

                when (val result = fetchSoundsForPathUseCase(path)) {
                    is FetchSoundsForPathResult.Success -> {
                        val sounds = soundsForFetchSoundsForPathSuccess(result)

                        deleteSoundsNotPresentInTheCloudFromInternalStorage(cloudSounds = sounds)

                        viewStateForInitiallyLoadedSounds(sounds)
                    }
                    is FetchSoundsForPathResult.Failure -> {
                        viewStateForFetchSoundsForPathFailure(
                            previousState = _viewState.value,
                            result = result
                        )
                    }
                }
            }

            _viewState.value = loadingDeferred.await()
        }
    }

    fun downloadAndSaveSoundLocally(sound: UiSound) {
        viewModelScope.launch {
            (_viewState.value as? SoundListViewState.SoundsLoaded)?.let { state ->
                val soundIndex = state.sounds.indexOf(sound)
                val downloadingSound = sound.copy(
                    downloadState = DownloadState.Downloading,
                )

                _viewState.value = SoundListViewState.SoundsLoaded.Active(
                    category = state.category,
                    sounds = state.sounds.withItemAtIndex(downloadingSound, soundIndex),
                    soundsDownloadState = state.soundsDownloadState,
                )

                when (val result = fetchByteArrayForPathUseCase(sound.path)) {
                    is FetchByteArrayForPathResult.Success -> {
                        val file =
                            fileWritingService.writeFileToInternalStorage(sound.path, result.bytes)

                        val downloadedSound = sound.copy(
                            downloadState = DownloadState.Downloaded,
                            localFile = file
                        )

                        _viewState.value = SoundListViewState.SoundsLoaded.Active(
                            category = state.category,
                            sounds = (_viewState.value as? SoundListViewState.SoundsLoaded)?.sounds?.withItemAtIndex(
                                downloadedSound,
                                soundIndex
                            ) ?: state.sounds,
                            soundsDownloadState = state.soundsDownloadState,
                        )
                    }
                    is FetchByteArrayForPathResult.Failure -> {
                        val soundWithErrorDownloading = sound.copy(
                            downloadState = DownloadState.ErrorDownloading
                        )

                        _viewState.value = SoundListViewState.SoundsLoaded.Error(
                            category = state.category,
                            sounds = (_viewState.value as? SoundListViewState.SoundsLoaded)?.sounds?.withItemAtIndex(
                                soundWithErrorDownloading,
                                soundIndex
                            ) ?: state.sounds, // TODO: it doesn't feel right
                            soundsDownloadState = state.soundsDownloadState,
                            errorMessage = errorMessageForFetchByteArrayForPathFailure(result)
                        )
                    }
                }
            }
        }
    }

    fun downloadSoundsAndSaveThemLocally() {
        viewModelScope.launch {
            (_viewState.value as? SoundListViewState.SoundsLoaded)?.let { state ->
                _viewState.value = SoundListViewState.SoundsLoaded.Active(
                    category = state.category,
                    sounds = state.sounds,
                    soundsDownloadState = DownloadState.Downloading,
                )

                val nonDownloadedSounds =
                    state.sounds.filter { !fileReadingService.fileExists(it.path) }

                for (nonDownloadedSound in nonDownloadedSounds) {
                    downloadAndSaveSoundLocally(nonDownloadedSound)
                }

                // TODO: Error?

                _viewState.value = SoundListViewState.SoundsLoaded.Active(
                    category = state.category,
                    sounds = state.sounds,
                    soundsDownloadState = DownloadState.Downloaded,
                )
            }
        }
    }

    fun playSound(sound: UiSound) {
        viewModelScope.launch {
            (_viewState.value as? SoundListViewState.SoundsLoaded)?.let { state ->
                var fileToPlay = sound.localFile
                if (fileToPlay == null) {
                    when (val result = fetchByteArrayForPathUseCase(sound.path)) {
                        is FetchByteArrayForPathResult.Success -> {
                            fileToPlay = clearCacheAndCreateCachedFile(sound, result.bytes)
                        }
                        is FetchByteArrayForPathResult.Failure -> {
                            _viewState.value = viewStateForFetchByteArrayForPathFailure(
                                previousState = state,
                                result = result,
                            )
                        }
                    }
                }

                fileToPlay?.let { playViaMediaPlayer(fileToPlay) }
            }
        }
    }

    fun toggleFavouriteSound(sound: UiSound) {
        (_viewState.value as? SoundListViewState.SoundsLoaded)?.let { state ->
            val soundIndex = state.sounds.indexOf(sound)
            val newSound = sound.copy(
                isFavourite = !sound.isFavourite
            )

            _viewState.value = SoundListViewState.SoundsLoaded.Active(
                category = state.category,
                sounds = state.sounds.withItemAtIndex(newSound, soundIndex),
                soundsDownloadState = state.soundsDownloadState,
            )
        }
    }

    fun shareSound(sound: UiSound) {
        viewModelScope.launch {
            (_viewState.value as? SoundListViewState.SoundsLoaded)?.let { state ->
                var fileToShare = sound.localFile
                if (fileToShare == null) {
                    when (val result = fetchByteArrayForPathUseCase(sound.path)) {
                        is FetchByteArrayForPathResult.Success -> {
                            fileToShare = clearCacheAndCreateCachedFile(sound, result.bytes)
                        }
                        is FetchByteArrayForPathResult.Failure -> {
                            _viewState.value = viewStateForFetchByteArrayForPathFailure(
                                previousState = state,
                                result = result,
                            )
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
    }

    fun resetToActiveSoundsLoaded() {
        viewModelScope.launch {
            (_viewState.value as? SoundListViewState.SoundsLoaded)?.let { state ->
                _viewState.value = SoundListViewState.SoundsLoaded.Active(
                    category = state.category,
                    sounds = state.sounds,
                    soundsDownloadState = state.soundsDownloadState
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

    private fun deleteSoundsNotPresentInTheCloudFromInternalStorage(cloudSounds: List<UiSound>) {
        val cloudSoundsNames = cloudSounds.map { it.fileName }

        val files =
            fileReadingService.readAllFilesInDirectory(_viewState.value.category.absolutePath)

        val filesToDelete = files.filter { !cloudSoundsNames.contains(it.name) }

        for (fileToDelete in filesToDelete) {
            fileDeletingService.deleteFile(fileToDelete)
        }
    }

    private fun clearCacheAndCreateCachedFile(sound: UiSound, bytes: ByteArray): File {
        fileDeletingService.clearCache()

        return fileWritingService.writeFileToCachedStorage(sound.fileName, bytes)
    }

    private fun soundsForFetchSoundsForPathSuccess(
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
        val categoryDownloadState =
            if (allDownloaded) DownloadState.Downloaded else DownloadState.NotDownloaded

        return SoundListViewState.SoundsLoaded.Active(
            category = _viewState.value.category,
            sounds = sounds,
            soundsDownloadState = categoryDownloadState
        )
    }

    private fun viewStateForFetchSoundsForPathFailure(
        previousState: SoundListViewState,
        result: FetchSoundsForPathResult.Failure
    ): SoundListViewState {
        return SoundListViewState.LoadingSoundsError(
            category = previousState.category,
            errorMessage = errorMessageForFetchSoundsForPathFailure(result)
        )
    }

    private fun errorMessageForFetchSoundsForPathFailure(
        result: FetchSoundsForPathResult.Failure,
    ): UiText {
        return when (result) {
            FetchSoundsForPathResult.Failure.NoNetwork ->
                UiText.ResourceText(R.string.networkErrorMessage)
            FetchSoundsForPathResult.Failure.ExceededFreeTier ->
                UiText.ResourceText(R.string.exceededFreeTierErrorMessage)
            FetchSoundsForPathResult.Failure.Unexpected ->
                UiText.ResourceText(R.string.unexpectedErrorMessage)
            FetchSoundsForPathResult.Failure.Unknown ->
                UiText.ResourceText(R.string.unknownErrorMessage)
        }
    }

    private fun viewStateForFetchByteArrayForPathFailure(
        previousState: SoundListViewState.SoundsLoaded,
        result: FetchByteArrayForPathResult.Failure,
    ): SoundListViewState {
        return SoundListViewState.SoundsLoaded.Error(
            category = previousState.category,
            sounds = previousState.sounds,
            soundsDownloadState = previousState.soundsDownloadState,
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
