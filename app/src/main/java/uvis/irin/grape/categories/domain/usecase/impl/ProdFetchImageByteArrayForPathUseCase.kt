package uvis.irin.grape.categories.domain.usecase.impl

import android.util.Log
import com.google.firebase.storage.StorageException
import javax.inject.Inject
import uvis.irin.grape.categories.data.repository.CategoryRepository
import uvis.irin.grape.categories.domain.model.result.FetchImageByteArrayForPathResult
import uvis.irin.grape.categories.domain.usecase.FetchImageByteArrayForPathUseCase
import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.core.data.StorageExceptionError

class ProdFetchImageByteArrayForPathUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : FetchImageByteArrayForPathUseCase {
    companion object {
        private const val TAG = "ProdFetchImageByteArrayForPathUseCase"
    }

    override suspend fun invoke(path: String): FetchImageByteArrayForPathResult {
        return when (val result = categoryRepository.fetchImageByteArrayForPath(path)) {
            is DataResult.Success -> {
                val bytes = result.data
                FetchImageByteArrayForPathResult.Success(bytes)
            }
            is DataResult.Failure -> {
                val failure = result.failure
                Log.d(TAG, "DataResult.Failure", failure)
                when (failure) {
                    is StorageException -> {
                        when (failure.errorCode) {
                            StorageExceptionError.QuotaExceeded.errorCode ->
                                FetchImageByteArrayForPathResult.Failure.ExceededFreeTier
                            StorageExceptionError.RetryLimitExceeded.errorCode ->
                                FetchImageByteArrayForPathResult.Failure.NoNetwork
                            StorageExceptionError.Unknown.errorCode ->
                                FetchImageByteArrayForPathResult.Failure.Unknown
                            else -> FetchImageByteArrayForPathResult.Failure.Unexpected
                        }
                    }
                    is IndexOutOfBoundsException -> FetchImageByteArrayForPathResult.Failure.TooLargeFile
                    else -> FetchImageByteArrayForPathResult.Failure.Unknown
                }
            }
        }
    }
}
