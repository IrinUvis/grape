package uvis.irin.grape.soundlist.domain.model.result

import uvis.irin.grape.soundlist.domain.model.DomainSound

sealed class FetchSoundsForPathResult {

    data class Success(
        val sounds: List<DomainSound>,
    ) : FetchSoundsForPathResult()

    sealed class Failure : FetchSoundsForPathResult() {

        object NoNetwork : Failure()

        object ExceededFreeTier : Failure()

        object Unexpected : Failure()

        object Unknown : Failure()
    }
}
