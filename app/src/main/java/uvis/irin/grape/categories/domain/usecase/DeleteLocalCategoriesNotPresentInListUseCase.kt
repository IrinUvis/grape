package uvis.irin.grape.categories.domain.usecase

import uvis.irin.grape.categories.domain.model.DomainCategory

interface DeleteLocalCategoriesNotPresentInListUseCase {

    suspend operator fun invoke(path: String, categoryList: List<DomainCategory>)
}
