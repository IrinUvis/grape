package uvis.irin.grape.soundlist.domain.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.core.di.IoDispatcher
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory
import java.io.IOException
import java.util.*
import javax.inject.Inject

class ProdSoundListRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : SoundListRepository {

    companion object {
        const val ASSET_SOUNDS_PATH = "sounds"
    }

    override suspend fun fetchAllCategories(): Result<List<SoundCategory>> {
        return withContext(coroutineDispatcher) {
            val am = context.assets

            // IDE flags this, but it should be fine
            // https://stackoverflow.com/questions/59684138/android-and-kotlin-coroutines-inappropriate-blocking-method-call
            am.list(ASSET_SOUNDS_PATH)?.let { directories ->
                Result.Success(
                    data = directories.map { directory ->
                        val subcategories =
                            am.list("$ASSET_SOUNDS_PATH/$directory")
                                ?.filter { !it.endsWith(".mp3") }
                                .takeIf { it.orEmpty().isNotEmpty() }
                                ?.map {
                                    SoundCategory(
                                        name = it.split('_').last().replaceFirstChar { char ->
                                            if (char.isLowerCase()) char.titlecase(
                                                Locale.getDefault()
                                            ) else char.toString()
                                        },
                                        subcategories = null,
                                        assetsPath = "$ASSET_SOUNDS_PATH/$directory/$it"
                                    )
                                }

                        SoundCategory(
                            name = directory.split('_').last().replaceFirstChar { char ->
                                if (char.isLowerCase()) char.titlecase(
                                    Locale.ROOT
                                ) else char.toString()
                            },
                            subcategories = subcategories,
                            assetsPath = "$ASSET_SOUNDS_PATH/$directory"
                        )
                    },
                )
            } ?: Result.Error(error = IOException("Cannot read assets"))
        }
    }


    override suspend fun fetchSoundsByCategory(category: SoundCategory): Result<List<Sound>> {

        return withContext(coroutineDispatcher) {
            val am = context.assets

            // IDE flags this, but it should be fine
            // https://stackoverflow.com/questions/59684138/android-and-kotlin-coroutines-inappropriate-blocking-method-call
            val filenames = am.list(category.assetsPath)

            filenames?.filter { it.endsWith(".mp3") }?.let { soundFilenames ->
                Result.Success(
                    data = soundFilenames.map {
                        ResourceSound(
                            name = it
                                .split('_').last()
                                .replaceFirstChar { char -> char.uppercase() }
                                .replace("\\.\\w+$".toRegex(), ""),
                            category = category,
                            relativeAssetPath = it
                        )
                    }
                )
            } ?: Result.Error(error = IOException("Cannot read assets"))
        }
    }
}
