package uvis.irin.grape.categories.domain.usecase.impl

import android.util.Log
import com.google.firebase.storage.StorageException
import javax.inject.Inject
import uvis.irin.grape.categories.data.repository.CategoryRepository
import uvis.irin.grape.categories.domain.model.result.FetchCategoriesForPathResult
import uvis.irin.grape.categories.domain.model.toDomainCategory
import uvis.irin.grape.categories.domain.usecase.FetchCategoriesForPathUseCase
import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.core.data.StorageExceptionError

class ProdFetchCategoriesForPathUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : FetchCategoriesForPathUseCase {
    companion object {
        private const val TAG = "ProdFetchCategoriesForPathUseCase"
    }

    override suspend fun invoke(path: String): FetchCategoriesForPathResult {
        return when (val result = categoryRepository.fetchCategoriesForPath(path)) {
            is DataResult.Success -> {
                val categories = result.data.map { it.toDomainCategory() }
                FetchCategoriesForPathResult.Success(categories)
            }
            is DataResult.Failure -> {
                val failure = result.failure
                Log.d(TAG, "DataResult.Failure", failure)
                when (failure) {
                    is StorageException -> {
                        when (failure.errorCode) {
                            StorageExceptionError.QuotaExceeded.errorCode ->
                                FetchCategoriesForPathResult.Failure.ExceededFreeTier
                            StorageExceptionError.RetryLimitExceeded.errorCode ->
                                FetchCategoriesForPathResult.Failure.NoNetwork
                            StorageExceptionError.Unknown.errorCode ->
                                FetchCategoriesForPathResult.Failure.Unknown
                            else -> FetchCategoriesForPathResult.Failure.Unexpected
                        }
                    }
                    else -> FetchCategoriesForPathResult.Failure.Unknown
                }
            }
        }
    }
}
