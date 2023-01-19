package uvis.irin.grape.soundlist.domain.usecase.impl

import android.util.Log
import com.google.firebase.storage.StorageException
import javax.inject.Inject
import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.core.data.StorageExceptionError
import uvis.irin.grape.soundlist.data.repository.SoundRepository
import uvis.irin.grape.soundlist.domain.model.result.FetchByteArrayForPathResult
import uvis.irin.grape.soundlist.domain.usecase.FetchByteArrayForPathUseCase

class ProdFetchByteArrayForPathUseCase @Inject constructor(
    private val soundRepository: SoundRepository,
) : FetchByteArrayForPathUseCase {
    companion object {
        private const val TAG = "ProdFetchDownloadUrlForPathUseCase"
    }

    override suspend fun invoke(path: String): FetchByteArrayForPathResult {
        return when (val result = soundRepository.fetchByteArrayForPath(path)) {
            is DataResult.Success -> {
                val bytes = result.data
                FetchByteArrayForPathResult.Success(bytes)
            }
            is DataResult.Failure -> {
                val failure = result.failure
                Log.d(TAG, "DataResult.Failure", failure)
                when (failure) {
                    is StorageException -> {
                        when (failure.errorCode) {
                            StorageExceptionError.QuotaExceeded.errorCode ->
                                FetchByteArrayForPathResult.Failure.ExceededFreeTier
                            StorageExceptionError.RetryLimitExceeded.errorCode ->
                                FetchByteArrayForPathResult.Failure.NoNetwork
                            StorageExceptionError.Unknown.errorCode -> FetchByteArrayForPathResult.Failure.Unknown
                            else -> FetchByteArrayForPathResult.Failure.Unexpected
                        }
                    }
                    is IndexOutOfBoundsException -> FetchByteArrayForPathResult.Failure.TooLargeFile
                    else -> {
                        FetchByteArrayForPathResult.Failure.Unknown
                    }
                }
            }
        }
    }
}