package uvis.irin.grape.categories.domain.usecase

import uvis.irin.grape.categories.domain.model.DomainCategory

interface FetchLocalCategoriesForPathUseCase {

    suspend operator fun invoke(path: String): List<DomainCategory>
}
