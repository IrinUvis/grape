package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory

interface GetAllSoundsByCategoryUseCase {
    operator fun invoke(category: SoundCategory): Result<List<Sound>>
}
