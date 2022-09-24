package uvis.irin.grape.soundlist.domain.repository

import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory

interface SoundListRepository {

    suspend fun fetchAllCategories(): Result<List<ResourceSoundCategory>>

    suspend fun fetchSoundsByCategory(category: ResourceSoundCategory): Result<List<ResourceSound>>
}
