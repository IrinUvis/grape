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
import uvis.irin.grape.core.extension.withDashesReplacedByForwardSlashes
import uvis.irin.grape.core.extension.withItemAtIndex
import uvis.irin.grape.core.ui.helpers.UiText
import uvis.irin.grape.navigation.SOUND_LIST_ARG
import uvis.irin.grape.soundlist.domain.model.DomainFavouriteSoundPath
import uvis.irin.grape.soundlist.domain.model.DomainSound
import uvis.irin.grape.soundlist.domain.model.result.FetchByteArrayForPathResult
import uvis.irin.grape.soundlist.domain.model.result.FetchSoundsForPathResult
import uvis.irin.grape.soundlist.domain.usecase.AddFavouriteSoundUseCase
import uvis.irin.grape.soundlist.domain.usecase.ClearCachedSoundsUseCase
import uvis.irin.grape.soundlist.domain.usecase.CreateCacheSoundFileUseCase
import uvis.irin.grape.soundlist.domain.usecase.DeleteFavouriteSoundUseCase
import uvis.irin.grape.soundlist.domain.usecase.DeleteFavouriteSoundsNotPresentInListUseCase
import uvis.irin.grape.soundlist.domain.usecase.DeleteLocalSoundsNotPresentInListUseCase
import uvis.irin.grape.soundlist.domain.usecase.DeleteSoundFileUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchFavouriteSoundsUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchLocalSoundFileForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchLocalSoundsForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchSoundByteArrayForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.FetchSoundsForPathUseCase
import uvis.irin.grape.soundlist.domain.usecase.GetFileExistsUseCase
import uvis.irin.grape.soundlist.domain.usecase.SaveSoundLocallyUseCase
import uvis.irin.grape.soundlist.domain.usecase.ShareSoundUseCase
import uvis.irin.grape.soundlist.ui.model.DownloadState
import uvis.irin.grape.soundlist.ui.model.UiSound
import uvis.irin.grape.soundlist.ui.model.toDomainSound
import uvis.irin.grape.soundlist.ui.model.toUiSound

@Suppress("TooManyFunctions")
@HiltViewModel
class SoundListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fetchSoundsForPathUseCase: FetchSoundsForPathUseCase,
    private val fetchSoundByteArrayForPathUseCase: FetchSoundByteArrayForPathUseCase,
    private val fetchLocalSoundsForPathUseCase: FetchLocalSoundsForPathUseCase,
    private val fetchLocalSoundFileForPathUseCase: FetchLocalSoundFileForPathUseCase,
    private val saveSoundLocallyUseCase: SaveSoundLocallyUseCase,
    private val deleteSoundFileUseCase: DeleteSoundFileUseCase,
    private val deleteLocalSoundsNotPresentInListUseCase: DeleteLocalSoundsNotPresentInListUseCase,
    private val clearCachedSoundsUseCase: ClearCachedSoundsUseCase,
    private val createCacheSoundFileUseCase: CreateCacheSoundFileUseCase,
    private val shareSoundUseCase: ShareSoundUseCase,
    private val getFileExistsUseCase: GetFileExistsUseCase,
    private val addFavouriteSoundUseCase: AddFavouriteSoundUseCase,
    private val deleteFavouriteSoundUseCase: DeleteFavouriteSoundUseCase,
    private val fetchFavouriteSoundsUseCase: FetchFavouriteSoundsUseCase,
    private val deleteFavouriteSoundsNotPresentInListUseCase: DeleteFavouriteSoundsNotPresentInListUseCase
) : ViewModel() {
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
        )
    )
    val viewState: StateFlow<SoundListViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch { loadSounds() }
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

            _viewState.update { it.forUpdatedSoundsDownloadingState() }
        }
    }

    fun downloadOrRemoveAllSounds() {
        viewModelScope.launch {
            if (_viewState.value.soundsDownloadState == DownloadState.NotDownloaded) {
                _viewState.update { it.copy(soundsDownloadState = DownloadState.Downloading) }

                _viewState.value.sounds?.let { sounds ->
                    val nonDownloadedSounds = sounds.filter { !getFileExistsUseCase(it.path) }

                    nonDownloadedSounds.map { downloadSoundAndSaveIt(it) }
                }

                _viewState.update { it.forUpdatedSoundsDownloadingState() }
            } else {
                _viewState.value.sounds?.let { sounds ->
                    val downloadedSounds = sounds.filter { getFileExistsUseCase(it.path) }

                    downloadedSounds.map { removeSoundFile(it) }
                }

                _viewState.update { it.forUpdatedSoundsDownloadingState() }
            }
        }
    }

    fun playSound(sound: UiSound) {
        viewModelScope.launch {
            var fileToPlay = sound.localFile
            if (fileToPlay == null) {
                when (val result = fetchSoundByteArrayForPathUseCase(sound.path)) {
                    is FetchByteArrayForPathResult.Success -> {
                        fileToPlay = clearCacheAndCreateCachedFile(result.bytes)
                    }
                    is FetchByteArrayForPathResult.Failure -> {
                        _viewState.update {
                            it.forSoundLoadingError(
                                errorMessage = errorMessageForFetchByteArrayForPathFailure(result)
                            )
                        }
                    }
                }
            }

            fileToPlay?.let { playViaMediaPlayer(fileToPlay) }
        }
    }

    fun toggleFavouriteSound(sound: UiSound) {
        viewModelScope.launch {
            _viewState.value.sounds?.let { sounds ->
                addToOrRemoveFromFavourites(sound)

                val soundIndex = sounds.indexOf(sound)
                val newSound = sound.copy(isFavourite = !sound.isFavourite)

                _viewState.update { it.copy(sounds = sounds.withItemAtIndex(newSound, soundIndex)) }
            }
        }
    }

    fun shareSound(sound: UiSound) {
        viewModelScope.launch {
            var fileToShare = sound.localFile
            if (fileToShare == null) {
                when (val result = fetchSoundByteArrayForPathUseCase(sound.path)) {
                    is FetchByteArrayForPathResult.Success -> {
                        fileToShare = clearCacheAndCreateCachedFile(result.bytes)
                    }
                    is FetchByteArrayForPathResult.Failure -> {
                        _viewState.update {
                            it.forSoundLoadingError(
                                errorMessage = errorMessageForFetchByteArrayForPathFailure(result)
                            )
                        }
                    }
                }
            }

            fileToShare?.let { soundFile -> shareSoundUseCase(soundFile) }
        }
    }

    fun retryLoadingSounds() {
        viewModelScope.launch {
            _viewState.update {
                it.copy(soundsLoadingState = SoundsLoadingState.Loading, errorMessage = null)
            }

            loadSounds()
        }
    }

    fun clearSearchQuery() {
        changeSearchQuery("")
    }

    fun changeSearchQuery(newText: String) {
        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    searchQuery = newText
                )
            }
        }
    }

    fun toggleSearchInput() {
        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    isSearchExpanded = !it.isSearchExpanded
                )
            }
        }
    }

    fun toggleShowOnlyFavourites() {
        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    showOnlyFavourites = !it.showOnlyFavourites,
                )
            }
        }
    }

    fun clearErrorMessage() {
        _viewState.update { it.copy(errorMessage = null) }
    }

    private suspend fun loadSounds() {
        val favouriteSoundsPaths = fetchFavouriteSoundsUseCase(categoryPath).map { it.path }
        val localSounds = fetchLocalSoundsForPathUseCase(categoryPath)

        when {
            localSounds.isNotEmpty() -> {
                fullLocalSoundsLoadWithSynchronization(
                    baseLocalSounds = localSounds,
                    favouriteSoundsPaths = favouriteSoundsPaths,
                )
            }
            else -> {
                initialRemoteSoundLoad(favouriteSoundsPaths = favouriteSoundsPaths)
            }
        }
    }

    private suspend fun fullLocalSoundsLoadWithSynchronization(
        baseLocalSounds: List<DomainSound>,
        favouriteSoundsPaths: List<String>
    ) {
        val loadedOfflineSounds = baseLocalSounds.map {
            val isFavourite = favouriteSoundsPaths.contains(it.path)
            val downloadState = DownloadState.Downloaded
            val localFile = fetchLocalSoundFileForPathUseCase(it.path)

            it.toUiSound(
                isFavourite = isFavourite,
                downloadState = downloadState,
                localFile = localFile
            )
        }

        _viewState.update {
            it.forLoadedSounds(
                sounds = loadedOfflineSounds,
                isSynchronizing = true
            )
        }

        when (val fetchRemoteSoundsResult = fetchSoundsForPathUseCase(categoryPath)) {
            is FetchSoundsForPathResult.Success -> {
                val remoteSounds = getSoundsWithFilesLoadedLocallyAndWithIsFavourites(
                    sounds = fetchRemoteSoundsResult.sounds.map { it.toUiSound() },
                    favouriteSoundsPaths = favouriteSoundsPaths,
                )

                _viewState.update { it.forLoadedSounds(sounds = remoteSounds) }

                deleteLocalSoundsNotPresentInListUseCase(
                    path = categoryPath,
                    soundList = remoteSounds.map { it.toDomainSound() },
                )

                deleteFavouriteSoundsNotPresentInListUseCase(
                    sounds = remoteSounds.map { it.toDomainSound() },
                    favouriteSoundPaths = favouriteSoundsPaths.map {
                        DomainFavouriteSoundPath(
                            path = it
                        )
                    },
                )

                _viewState.update { it.copy(isSynchronizing = false) }
            }
            is FetchSoundsForPathResult.Failure -> {
                _viewState.update { it.copy(isSynchronizing = false) }
            }
        }
    }

    private suspend fun initialRemoteSoundLoad(favouriteSoundsPaths: List<String>) {
        when (val fetchRemoteSoundsResult = fetchSoundsForPathUseCase(categoryPath)) {
            is FetchSoundsForPathResult.Success -> {
                val resultSounds = fetchRemoteSoundsResult.sounds.map { it.toUiSound() }

                if (resultSounds.isEmpty()) {
                    _viewState.update { it.copy(soundsLoadingState = SoundsLoadingState.ShouldBeCategory) }
                } else {
                    val remoteSounds = getSoundsWithFilesLoadedLocallyAndWithIsFavourites(
                        sounds = resultSounds,
                        favouriteSoundsPaths = favouriteSoundsPaths,
                    )

                    _viewState.update {
                        it.forLoadedSounds(
                            sounds = remoteSounds,
                        )
                    }
                }
            }
            is FetchSoundsForPathResult.Failure -> {
                _viewState.update {
                    it.forLoadingError(
                        errorMessage = errorMessageForFetchSoundsForPathFailure(
                            fetchRemoteSoundsResult
                        )
                    )
                }
            }
        }
    }

    private suspend fun downloadSoundAndSaveIt(sound: UiSound) {
        val downloadingSound = sound.copy(downloadState = DownloadState.Downloading)
        _viewState.value.sounds?.let { sounds ->
            val soundIndex = sounds.indexOf(sound)

            _viewState.update {
                it.copy(
                    sounds = sounds.withItemAtIndex(
                        downloadingSound,
                        soundIndex
                    )
                )
            }
        }

        val soundPath = sound.path

        when (val result = fetchSoundByteArrayForPathUseCase(soundPath)) {
            is FetchByteArrayForPathResult.Success -> {
                val file = saveSoundLocallyUseCase(soundPath, result.bytes)

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
        deleteSoundFileUseCase(sound.path)

        _viewState.value.sounds?.let { sounds ->
            val soundIndex = sounds.indexOf(sound)
            val notDownloadedSound = sound.copy(
                downloadState = DownloadState.NotDownloaded,
                localFile = null,
            )

            _viewState.update {
                it.copy(
                    sounds = sounds.withItemAtIndex(
                        notDownloadedSound,
                        soundIndex
                    )
                )
            }
        }
    }

    private suspend fun addToOrRemoveFromFavourites(sound: UiSound) {
        val path = sound.path
        if (sound.isFavourite) {
            deleteFavouriteSoundUseCase(path)
        } else {
            addFavouriteSoundUseCase(path)
        }
    }

    private fun playViaMediaPlayer(file: File) {
        mediaPlayer.reset()
        val fis = FileInputStream(file)
        mediaPlayer.setDataSource(fis.fd)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    private suspend fun clearCacheAndCreateCachedFile(bytes: ByteArray): File {
        clearCachedSoundsUseCase()

        return createCacheSoundFileUseCase(bytes)
    }

    private suspend fun getSoundsWithFilesLoadedLocallyAndWithIsFavourites(
        sounds: List<UiSound>,
        favouriteSoundsPaths: List<String>,
    ): List<UiSound> {
        val remoteSoundsWithoutFavourites = getSoundsWithFilesLoadedLocally(sounds = sounds)

        return getSoundsWithIsFavourites(
            sounds = remoteSoundsWithoutFavourites,
            favouriteSoundsPaths = favouriteSoundsPaths
        )
    }

    private suspend fun getSoundsWithFilesLoadedLocally(
        sounds: List<UiSound>
    ): List<UiSound> {
        return sounds.map {
            val soundFile = fetchLocalSoundFileForPathUseCase(it.path)
            val fileDownloaded = soundFile != null
            val downloadState =
                if (fileDownloaded) DownloadState.Downloaded else DownloadState.NotDownloaded
            it.copy(
                downloadState = downloadState,
                localFile = soundFile,
            )
        }
    }

    private fun getSoundsWithIsFavourites(
        sounds: List<UiSound>,
        favouriteSoundsPaths: List<String>,
    ): List<UiSound> {
        return sounds.map { it.copy(isFavourite = favouriteSoundsPaths.contains(it.path)) }
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

private fun SoundListViewState.forLoadedSounds(
    sounds: List<UiSound>,
    isSynchronizing: Boolean? = null,
): SoundListViewState {
    val allDownloaded = sounds.none { it.downloadState == DownloadState.NotDownloaded }
    val soundsDownloadState =
        if (allDownloaded) DownloadState.Downloaded else DownloadState.NotDownloaded

    return copy(
        sounds = sounds,
        soundsLoadingState = SoundsLoadingState.Loaded,
        isSynchronizing = isSynchronizing ?: this.isSynchronizing,
        soundsDownloadState = soundsDownloadState,
    )
}

private fun SoundListViewState.forUpdatedSoundsDownloadingState(): SoundListViewState {
    return if (sounds != null) {
        val allDownloaded = sounds.all { it.downloadState == DownloadState.Downloaded }

        val soundsDownloadState =
            if (allDownloaded) DownloadState.Downloaded else DownloadState.NotDownloaded

        copy(soundsDownloadState = soundsDownloadState)
    } else this
}

private fun SoundListViewState.forLoadingError(
    errorMessage: UiText
) = copy(
    soundsLoadingState = SoundsLoadingState.LoadingError,
    errorMessage = errorMessage
)

private fun SoundListViewState.forSoundLoadingError(
    errorMessage: UiText
) = copy(
    soundsLoadingState = SoundsLoadingState.Loaded,
    errorMessage = errorMessage
)
