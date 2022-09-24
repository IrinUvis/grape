package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory
import uvis.irin.grape.soundlist.domain.repository.SoundListRepository
import javax.inject.Inject

class ProdGetAllSoundsByCategoryUseCase @Inject constructor(
    private val soundListRepository: SoundListRepository,
) : GetAllSoundsByCategoryUseCase {

    override suspend fun invoke(category: ResourceSoundCategory): Result<List<ResourceSound>> {
        return soundListRepository.fetchSoundsByCategory(category)
    }
}
