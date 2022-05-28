package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory
import uvis.irin.grape.soundlist.domain.repository.ProdSoundListRepository
import javax.inject.Inject

class ProdGetAllSoundsByCategoryUseCase @Inject constructor(
    private val soundListRepository: ProdSoundListRepository,
) : GetAllSoundsByCategoryUseCase {

    override fun invoke(category: SoundCategory): Result<List<Sound>> {
        return soundListRepository.fetchSoundsByCategory(category)
    }
}
