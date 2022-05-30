package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory
import uvis.irin.grape.soundlist.domain.repository.SoundListRepository
import javax.inject.Inject

class ProdGetAllSoundsByCategoryUseCase @Inject constructor(
    private val soundListRepository: SoundListRepository,
) : GetAllSoundsByCategoryUseCase {

    override suspend fun invoke(category: SoundCategory): Result<List<Sound>> {
        return soundListRepository.fetchSoundsByCategory(category)
    }
}
