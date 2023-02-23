package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.soundlist.domain.model.result.FetchByteArrayForPathResult

interface FetchSoundByteArrayForPathUseCase {

    suspend operator fun invoke(path: String): FetchByteArrayForPathResult
}
