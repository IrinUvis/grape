package uvis.irin.grape.soundlist.domain.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory
import javax.inject.Inject

class ProdSoundListRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : SoundListRepository {

    override suspend fun fetchAllCategories(): Result<List<SoundCategory>> {
        @Suppress("MagicNumber")
        delay(2_000)

        return Result.Success(
            data = listOf(
                SoundCategory(name = "Stonoga", assetsPath = "stonoga"),
                SoundCategory(name = "Jagoda", assetsPath = "jagoda"),
                SoundCategory(name = "Brozi", assetsPath = "brozi"),
                SoundCategory(name = "Polska", assetsPath = "polska"),
                SoundCategory(name = "Jail", assetsPath = "jail"),
                SoundCategory(name = "Ochelska", assetsPath = "ochelska"),
                SoundCategory(name = "CKaE", assetsPath = "ckae"),
            ),
        )
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
