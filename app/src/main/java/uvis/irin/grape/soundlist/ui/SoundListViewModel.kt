package uvis.irin.grape.soundlist.ui

import android.annotation.SuppressLint
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
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.usecase.GetAllSoundsByCategoryUseCase
import uvis.irin.grape.soundlist.domain.usecase.GetSoundCategoriesUseCase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class SoundListViewModel @Inject constructor(
    private val getSoundCategoriesUseCase: GetSoundCategoriesUseCase,
    private val getAllSoundsByCategoryUseCase: GetAllSoundsByCategoryUseCase
) : ViewModel() {
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    private val _viewState: MutableStateFlow<SoundListViewState> =
        MutableStateFlow(SoundListViewState())
    val viewState: StateFlow<SoundListViewState> = _viewState

    init {
        viewModelScope.launch {
            val getSoundCategoriesResult = getSoundCategoriesUseCase()

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
            mediaPlayer.reset()
            val descriptor =
                context.assets.openFd(sound.completePath)
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

    @SuppressLint("QueryPermissionsNeeded")
    fun onSoundLongPressed(sound: Sound, context: Context) {
        if (sound is ResourceSound) {
            try {
                val inStream =
                    context.assets.open(sound.completePath)
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

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}
