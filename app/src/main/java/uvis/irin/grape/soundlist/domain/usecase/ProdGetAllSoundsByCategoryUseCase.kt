package uvis.irin.grape.soundlist.domain.usecase

import android.content.Context
import androidx.compose.ui.text.capitalize
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory
import uvis.irin.grape.soundlist.domain.repository.ProdSoundListRepository

class ProdGetAllSoundsByCategoryUseCase(
    private val soundListRepository: ProdSoundListRepository,
    private val context: Context
) : GetAllSoundsByCategoryUseCase {
    override fun invoke(category: SoundCategory): Result<List<Sound>> {
        return soundListRepository.fetchSoundsByCategory(category, context)
    }
}