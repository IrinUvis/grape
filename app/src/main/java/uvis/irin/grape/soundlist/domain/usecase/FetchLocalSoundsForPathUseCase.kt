package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.soundlist.domain.model.DomainSound

interface FetchLocalSoundsForPathUseCase {

    suspend operator fun invoke(path: String): List<DomainSound>
}
