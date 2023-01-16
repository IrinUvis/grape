package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory
import uvis.irin.grape.soundlist.domain.repository.SoundListRepository
import javax.inject.Inject

class ProdGetSoundCategoriesUseCase @Inject constructor(
    private val soundListRepository: SoundListRepository
) : GetSoundCategoriesUseCase {

    override suspend fun invoke(): DataResult<List<ResourceSoundCategory>> {
        return soundListRepository.fetchAllCategories()
    }
}
