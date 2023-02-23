package uvis.irin.grape.categories.domain.model.result

import uvis.irin.grape.categories.domain.model.DomainCategory

sealed class FetchCategoriesForPathResult {

    data class Success(
        val categories: List<DomainCategory>,
    ) : FetchCategoriesForPathResult()

    sealed class Failure : FetchCategoriesForPathResult() {

        object NoNetwork : Failure()

        object ExceededFreeTier : Failure()

        object Unexpected : Failure()

        object Unknown : Failure()
    }
}
