package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory

interface GetSoundCategoriesUseCase {

    suspend operator fun invoke(): DataResult<List<ResourceSoundCategory>>
}
