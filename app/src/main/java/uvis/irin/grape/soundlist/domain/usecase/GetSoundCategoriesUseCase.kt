package uvis.irin.grape.soundlist.domain.usecase

import kotlinx.coroutines.flow.Flow
import uvis.irin.grape.core.data.Result
import uvis.irin.grape.soundlist.domain.model.SoundCategory

interface GetSoundCategoriesUseCase {
    suspend operator fun invoke(): Result<List<SoundCategory>>
}