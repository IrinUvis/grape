package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.SoundCategory
import uvis.irin.grape.soundlist.domain.repository.SoundListRepository
import javax.inject.Inject

class ProdGetSoundCategoriesUseCase @Inject constructor(
    private val soundListRepository: SoundListRepository
) : GetSoundCategoriesUseCase {

    override suspend fun invoke(): Result<List<SoundCategory>> {
        return soundListRepository.fetchAllCategories()
    }
}
