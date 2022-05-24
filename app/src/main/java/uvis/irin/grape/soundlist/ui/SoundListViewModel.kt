package uvis.irin.grape.soundlist.ui

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

class SoundListViewModel(
    private val getSoundCategoriesUseCase: GetSoundCategoriesUseCase,
    private val getAllSoundsByCategoryUseCase: GetAllSoundsByCategoryUseCase,
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

    fun onSoundLongPressed(sound: Sound, context: Context) {
        if (sound is ResourceSound) {
            @Suppress("SwallowedException")
            try {
                val inStream = context.assets.open("${sound.category.assetsPath}/${sound.relativeAssetPath}")
                val soundTempFile = File.createTempFile("sound", ".mp3")
                copyFile(inStream, FileOutputStream(soundTempFile))

                val authority = BuildConfig.APPLICATION_ID + ".provider"
                val uri = FileProvider.getUriForFile(context.applicationContext, authority, soundTempFile)
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "audio/mp3"
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.startActivity(Intent.createChooser(shareIntent, "Share"))
            } catch (ex: IOException) {
                Log.e("Sound sharing", "${sound.category.assetsPath}/${sound.relativeAssetPath} cannot be shared")
            }
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
}
