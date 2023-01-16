package uvis.irin.grape.soundlist.domain.repository

import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory

interface SoundListRepository {

    suspend fun fetchAllCategories(): DataResult<List<ResourceSoundCategory>>

    suspend fun fetchSoundsByCategory(category: ResourceSoundCategory): DataResult<List<ResourceSound>>

    suspend fun fetchAllFavouriteSounds(): DataResult<List<ResourceSound>>

    suspend fun insertFavouriteSound(favouriteSound: ResourceSound)

    suspend fun deleteFavouriteSound(favouriteSound: ResourceSound)
}
