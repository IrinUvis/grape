package uvis.irin.grape.core.data

@Suppress("MagicNumber")
enum class StorageExceptionError(val errorCode: Int) {
    BucketNotFound(-13011),
    Canceled(-13040),
    InvalidChecksum(-13031),
    NotAuthenticated(-13020),
    NotAuthorized(-13021),
    ObjectNotFound(-13010),
    ProjectNotFound(-13012),
    QuotaExceeded(-13013),
    RetryLimitExceeded(-13030),
    Unknown(-13000)
}
