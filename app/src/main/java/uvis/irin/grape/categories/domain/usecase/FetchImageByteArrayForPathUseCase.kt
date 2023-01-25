package uvis.irin.grape.categories.domain.usecase

import uvis.irin.grape.categories.domain.model.result.FetchImageByteArrayForPathResult

interface FetchImageByteArrayForPathUseCase {

    suspend operator fun invoke(path: String): FetchImageByteArrayForPathResult
}
