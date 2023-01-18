package uvis.irin.grape.soundlist.domain.usecase.impl

import android.util.Log
import com.google.firebase.storage.StorageException
import javax.inject.Inject
import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.core.data.StorageExceptionError
import uvis.irin.grape.soundlist.data.repository.SoundRepository
import uvis.irin.grape.soundlist.domain.model.result.FetchSoundsForPathResult
import uvis.irin.grape.soundlist.domain.model.toDomainSound
import uvis.irin.grape.soundlist.domain.usecase.FetchSoundsForPathUseCase

class ProdFetchSoundsForPathUseCase @Inject constructor(
    private val soundRepository: SoundRepository,
) : FetchSoundsForPathUseCase {
    companion object {
        private const val TAG = "ProdFetchSoundsForPathUseCase"
    }

    override suspend fun invoke(path: String): FetchSoundsForPathResult {
        return when (val result = soundRepository.fetchSoundsForPath(path)) {
            is DataResult.Success -> {
                val sounds = result.data.map { it.toDomainSound() }
                FetchSoundsForPathResult.Success(sounds)
            }
            is DataResult.Failure -> {
                val failure = result.failure
                Log.e(TAG, "DataResult.Failure", failure)
                when (failure) {
                    is StorageException -> {
                        when (failure.errorCode) {
                            StorageExceptionError.QuotaExceeded.errorCode ->
                                FetchSoundsForPathResult.Failure.ExceededFreeTier
                            StorageExceptionError.RetryLimitExceeded.errorCode ->
                                FetchSoundsForPathResult.Failure.NoNetwork
                            StorageExceptionError.Unknown.errorCode -> FetchSoundsForPathResult.Failure.Unknown
                            else -> FetchSoundsForPathResult.Failure.Unexpected
                        }
                    }
                    else -> {
                        FetchSoundsForPathResult.Failure.Unknown
                    }
                }
            }
        }
    }
}
