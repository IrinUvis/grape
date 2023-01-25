package uvis.irin.grape.categories.domain.usecase

import uvis.irin.grape.categories.domain.model.result.FetchCategoriesForPathResult

interface FetchCategoriesForPathUseCase {

    suspend operator fun invoke(path: String): FetchCategoriesForPathResult
}
