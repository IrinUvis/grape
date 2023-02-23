package uvis.irin.grape.soundlist.domain.model.result

sealed class FetchByteArrayForPathResult {

    data class Success(
        val bytes: ByteArray,
    ) : FetchByteArrayForPathResult() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Success

            if (!bytes.contentEquals(other.bytes)) return false

            return true
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }

    sealed class Failure : FetchByteArrayForPathResult() {

        object NoNetwork : Failure()

        object ExceededFreeTier : Failure()

        object TooLargeFile : Failure()

        object Unexpected : Failure()

        object Unknown : Failure()
    }
}
