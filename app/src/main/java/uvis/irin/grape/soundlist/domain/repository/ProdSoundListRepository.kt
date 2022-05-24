package uvis.irin.grape.soundlist.domain.repository

import android.content.Context
import kotlinx.coroutines.delay
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory

class ProdSoundListRepository : SoundListRepository {

    override suspend fun fetchAllCategories(): Result<List<SoundCategory>> {
        @Suppress("MagicNumber")
        delay(2_000)

        return Result.Success(
            data = listOf(
                SoundCategory(name = "Stonoga", assetsPath = "stonoga"),
                SoundCategory(name = "Jagoda", assetsPath = "stonoga"),
                SoundCategory(name = "Brozi", assetsPath = "stonoga"),
                SoundCategory(name = "Polska", assetsPath = "stonoga"),
                SoundCategory(name = "Jail", assetsPath = "stonoga"),
                SoundCategory(name = "Ochelska", assetsPath = "stonoga"),
                SoundCategory(name = "CKaE", assetsPath = "stonoga"),
            ),
        )
    }

    override fun fetchSoundsByCategory(category: SoundCategory, context: Context): Result<List<Sound>> {
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
