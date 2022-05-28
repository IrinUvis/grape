package uvis.irin.grape.soundlist.domain.usecase

import android.content.Context
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory
import uvis.irin.grape.soundlist.domain.repository.ProdSoundListRepository

class ProdGetAllSoundsByCategoryUseCase(
    private val soundListRepository: ProdSoundListRepository,
) : GetAllSoundsByCategoryUseCase {

    override fun invoke(category: SoundCategory, context: Context): Result<List<Sound>> {
        return soundListRepository.fetchSoundsByCategory(category, context)
    }
}
