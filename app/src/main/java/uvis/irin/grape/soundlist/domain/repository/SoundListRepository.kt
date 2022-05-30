package uvis.irin.grape.soundlist.domain.repository

import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory

interface SoundListRepository {

    suspend fun fetchAllCategories(): Result<List<SoundCategory>>

    suspend fun fetchSoundsByCategory(category: SoundCategory): Result<List<Sound>>
}
