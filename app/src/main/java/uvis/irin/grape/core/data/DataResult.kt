package uvis.irin.grape.core.data

/**
 * Class holding the result of some operation.
 * If it is [Success], the operation finished successfully and the data can be accessed.
 * In case of [Failure], the operation finished with an error and the Exception that occurred can be read.
 */
sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()

    data class Failure(val failure: Throwable) : DataResult<Nothing>()
}
