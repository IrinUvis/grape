package uvis.irin.grape.categories.domain.usecase

interface FetchLocalImageByteArrayForPathUseCase {

    suspend operator fun invoke(path: String) : ByteArray?
}
