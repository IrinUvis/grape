package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory

interface GetAllSoundsByCategoryUseCase {

    suspend operator fun invoke(category: ResourceSoundCategory): DataResult<List<ResourceSound>>
}
