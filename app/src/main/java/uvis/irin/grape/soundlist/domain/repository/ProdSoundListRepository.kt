package uvis.irin.grape.soundlist.domain.repository

import kotlinx.coroutines.delay
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.SoundCategory

class ProdSoundListRepository : SoundListRepository {
    override suspend fun fetchAllCategories(): Result<List<SoundCategory>> {
        @Suppress("MagicNumber")
        delay(2_000)

        return Result.Success(
            data = listOf(
                SoundCategory(name = "Stonoga"),
                SoundCategory(name = "Jagoda"),
                SoundCategory(name = "Brozi"),
                SoundCategory(name = "Polska"),
                SoundCategory(name = "Jail"),
                SoundCategory(name = "Ochelska"),
                SoundCategory(name = "CKaE"),
            ),
        )
    }
}
