package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.soundlist.domain.model.result.FetchSoundsForPathResult

interface FetchSoundsForPathUseCase {

    suspend operator fun invoke(path: String): FetchSoundsForPathResult
}
