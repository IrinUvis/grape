package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory

interface GetSoundCategoriesUseCase {

    suspend operator fun invoke(): Result<List<ResourceSoundCategory>>
}
