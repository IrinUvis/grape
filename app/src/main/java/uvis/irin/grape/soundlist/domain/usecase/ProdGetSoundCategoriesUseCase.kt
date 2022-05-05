package uvis.irin.grape.soundlist.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.SoundCategory
import uvis.irin.grape.soundlist.domain.repository.SoundListRepository

class ProdGetSoundCategoriesUseCase(
    private val soundListRepository: SoundListRepository
) : GetSoundCategoriesUseCase {

    override suspend fun invoke(): Result<List<SoundCategory>> {
        return soundListRepository.fetchAllCategories()
    }
}