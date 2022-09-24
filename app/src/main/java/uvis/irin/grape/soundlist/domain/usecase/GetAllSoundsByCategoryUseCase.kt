package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory

interface GetAllSoundsByCategoryUseCase {

    suspend operator fun invoke(category: ResourceSoundCategory): Result<List<ResourceSound>>
}
