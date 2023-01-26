package uvis.irin.grape.categories.domain.usecase

interface SaveImageByteArrayLocallyUseCase {

    suspend operator fun invoke(path: String, byteArray: ByteArray)
}
