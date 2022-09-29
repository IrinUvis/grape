package uvis.irin.grape.soundlist.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.media.MediaPlayer
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uvis.irin.grape.BuildConfig
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory
import uvis.irin.grape.soundlist.domain.repository.ProdSoundListRepository
import uvis.irin.grape.soundlist.domain.usecase.GetAllSoundsByCategoryUseCase
import uvis.irin.grape.soundlist.domain.usecase.GetSoundCategoriesUseCase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@HiltViewModel
@Suppress("TooManyFunctions")
class SoundListViewModel @Inject constructor(
    private val getSoundCategoriesUseCase: GetSoundCategoriesUseCase,
    private val getAllSoundsByCategoryUseCase: GetAllSoundsByCategoryUseCase,
    private val soundListRepository: ProdSoundListRepository,
) : ViewModel() {
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    private val _viewState: MutableStateFlow<SoundListViewState> =
        MutableStateFlow(SoundListViewState())
    val viewState: StateFlow<SoundListViewState> = _viewState

    init {
        viewModelScope.launch {
            val getSoundCategoriesResult = withContext(Dispatchers.IO) {
                getSoundCategoriesUseCase()
            }

            val favouriteSoundsResult = withContext(Dispatchers.IO) {
                soundListRepository.fetchAllFavouriteSounds()
            }

            _viewState.value = getViewStateForAllSoundCategoriesResult(getSoundCategoriesResult)

            _viewState.value = getViewStateForAllFavouriteSoundsResult(favouriteSoundsResult)

            val initialCategory =
                if (_viewState.value.selectedSubcategory != null) _viewState.value.selectedSubcategory
                else _viewState.value.selectedCategory

            if (initialCategory != null) {
                val getAllSoundsByCategoryResult = withContext(Dispatchers.IO) {
                    getAllSoundsByCategoryUseCase(initialCategory)
                }

                _viewState.value =
                    getViewStateForAllSoundsByCategoryResult(getAllSoundsByCategoryResult)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }

    fun onSoundPressed(sound: ResourceSound, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            playSound(sound, context)
        }
    }

    fun onSoundShareButtonPressed(sound: ResourceSound, context: Context) {
        viewModelScope.launch {
            try {
                val inStream = withContext(Dispatchers.IO) {
                    context.assets.open(sound.completePath)

                }
                val soundTempFile = File.createTempFile("sound", ".mp3")
                copyFile(inStream, FileOutputStream(soundTempFile))

                val authority = BuildConfig.APPLICATION_ID + ".provider"
                val uri =
                    FileProvider.getUriForFile(context.applicationContext, authority, soundTempFile)

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "audio/mp3"
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)

                val chooser = Intent.createChooser(shareIntent, "Share sound")

                val resInfoList: List<ResolveInfo> = context.packageManager
                    .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    context.grantUriPermission(
                        packageName,
                        uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }

                context.startActivity(chooser)
            } catch (ex: IOException) {
                Log.e(
                    "Sound sharing",
                    "${sound.category.assetsPath}/${sound.relativeAssetPath} cannot be shared",
                    ex
                )
                _viewState.update {
                    it.copy(
                        errorMessage = "Cannot share the file"
                    )
                }
            }
        }
    }

    fun onCategorySelected(category: ResourceSoundCategory) {
        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    selectedCategory = category,
                    subcategories = category.subcategories,
                    selectedSubcategory = category.subcategories?.firstOrNull()
                )
            }

            val currentCategory =
                if (_viewState.value.selectedSubcategory != null) _viewState.value.selectedSubcategory
                else _viewState.value.selectedCategory

            if (currentCategory != null) {
                val getAllSoundsByCategoryResult =
                    withContext(Dispatchers.IO) {
                        getAllSoundsByCategoryUseCase(currentCategory)
                    }

                _viewState.value =
                    getViewStateForAllSoundsByCategoryResult(getAllSoundsByCategoryResult)
            }
        }
    }

    fun onSubcategorySelected(category: ResourceSoundCategory) {
        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    selectedSubcategory = category
                )
            }

            val getAllSoundsByCategoryResult = withContext(Dispatchers.IO) {
                getAllSoundsByCategoryUseCase(category)
            }

            _viewState.value =
                getViewStateForAllSoundsByCategoryResult(getAllSoundsByCategoryResult)
        }
    }

    fun onFavouriteButtonPressed(sound: ResourceSound) {
        viewModelScope.launch {
            if (viewState.value.favouriteSounds.contains(sound)) {
                soundListRepository.deleteFavouriteSound(sound)
            } else {
                soundListRepository.insertFavouriteSound(sound)
            }
            val updatedFavouriteSoundsResult = soundListRepository.fetchAllFavouriteSounds()
            _viewState.value = getViewStateForAllFavouriteSoundsResult(updatedFavouriteSoundsResult)
        }
    }

    fun onDisplayOnlyFavouritesButtonPressed() {
        _viewState.value = viewState.value.copy(
            displayOnlyFavourites = !viewState.value.displayOnlyFavourites
        )
    }

    fun onSoundSearchBarTextChanged(newText: String) {
        _viewState.value = viewState.value.copy(
            searchQuery = newText
        )
    }

    fun onBackButtonPressed(context: Context) {
        viewModelScope.launch {
            val goodbyeSound = ResourceSound(
                name = "Na razie",
                category = ResourceSoundCategory(
                    name = "Stonoga",
                    subcategories = null,
                    assetsPath = "sounds/6_qrwio vadis"
                ),
                relativeAssetPath = "Do widzenia.mp3"
            )

            playSound(
                sound = goodbyeSound,
                context = context,
                onCompletionListener = { (context as? Activity)?.finish() }
            )
        }
    }

    fun onErrorSnackbarDismissed() {
        _viewState.update {
            it.copy(
                errorMessage = null
            )
        }
    }

    @Throws(IOException::class)
    private fun copyFile(inStream: InputStream, outStream: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (inStream.read(buffer).also { read = it } != -1) {
            outStream.write(buffer, 0, read)
        }
    }

    private fun playSound(
        sound: ResourceSound,
        context: Context,
        onCompletionListener: () -> Unit = { }
    ) {
        mediaPlayer.reset()
        try {
            val descriptor =
                context.assets.openFd(sound.completePath)

            mediaPlayer.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )
            descriptor.close()

            mediaPlayer.prepare()
            mediaPlayer.setOnCompletionListener {
                onCompletionListener()
            }
            mediaPlayer.setVolume(1f, 1f)
            mediaPlayer.isLooping = false
            mediaPlayer.start()
        } catch (ex: IOException) {
            Log.e(
                "Sound playing",
                "${sound.category.assetsPath}/${sound.relativeAssetPath} cannot be played",
                ex
            )
            _viewState.update {
                it.copy(
                    errorMessage = "Cannot play sound"
                )
            }
        }
    }

    private fun getViewStateForAllSoundCategoriesResult(
        getSoundCategoriesResult: Result<List<ResourceSoundCategory>>
    ): SoundListViewState {
        return when (getSoundCategoriesResult) {
            is Result.Success -> {
                _viewState.value.copy(
                    categories = getSoundCategoriesResult.data,
                    subcategories = getSoundCategoriesResult.data.first().subcategories,
                    selectedCategory = getSoundCategoriesResult.data.first(),
                    selectedSubcategory = getSoundCategoriesResult.data.first().subcategories?.firstOrNull()
                )
            }
            is Result.Error -> {
                _viewState.value.copy(
                    categories = emptyList()
                )
            }
        }
    }

    private fun getViewStateForAllSoundsByCategoryResult(
        getAllSoundsByCategoryResult: Result<List<ResourceSound>>
    ): SoundListViewState {
        return when (getAllSoundsByCategoryResult) {
            is Result.Success -> {
                _viewState.value.copy(
                    showLoading = false,
                    sounds = getAllSoundsByCategoryResult.data
                )
            }
            is Result.Error -> {
                _viewState.value.copy(
                    showLoading = false,
                    sounds = emptyList()
                )
            }
        }
    }

    private fun getViewStateForAllFavouriteSoundsResult(
        fetchAllFavouriteSoundsResult: Result<List<ResourceSound>>
    ): SoundListViewState {
        return when (fetchAllFavouriteSoundsResult) {
            is Result.Success -> {
                _viewState.value.copy(
                    favouriteSounds = fetchAllFavouriteSoundsResult.data
                )
            }
            is Result.Error -> {
                _viewState.value.copy(
                    favouriteSounds = emptyList()
                )
            }
        }
    }
}
