package uvis.irin.grape.soundlist.domain.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
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
        const val ASSET_PATH = "sounds"
    }

    override suspend fun fetchAllCategories(): Result<List<SoundCategory>> {
        @Suppress("MagicNumber")
        delay(2_000)

        return withContext(coroutineDispatcher) {
            val am = context.assets

            // IDE flags this, but it should be fine
            // https://stackoverflow.com/questions/59684138/android-and-kotlin-coroutines-inappropriate-blocking-method-call
            val directories = am.list(ASSET_PATH)

            directories?.let { directory ->
                Result.Success(
                    data = directory.map {
                        SoundCategory(
                            name = it.replaceFirstChar { char ->
                                if (char.isLowerCase()) char.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            },
                            assetsPath = "$ASSET_PATH/$it"
                        )
                    },
                )
            } ?: Result.Error(error = IOException("Cannot read assets"))
        }
    }

    override fun fetchSoundsByCategory(category: SoundCategory): Result<List<Sound>> {
        val am = context.assets

        val sounds = am.list(category.assetsPath)?.map { filename ->
            ResourceSound(
                name = filename
                    .replaceFirstChar { it.uppercase() }
                    .replace("\\.\\w+$".toRegex(), ""),
                category = category,
                relativeAssetPath = filename
            )
        } ?: emptyList()

        return Result.Success(sounds)
    }
}
