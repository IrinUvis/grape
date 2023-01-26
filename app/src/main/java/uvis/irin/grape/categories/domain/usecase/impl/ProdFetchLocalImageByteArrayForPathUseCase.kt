package uvis.irin.grape.categories.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.categories.domain.usecase.FetchLocalImageByteArrayForPathUseCase
import uvis.irin.grape.core.android.service.file.FileReadingService

class ProdFetchLocalImageByteArrayForPathUseCase @Inject constructor(
    private val fileReadingService: FileReadingService,
) : FetchLocalImageByteArrayForPathUseCase {
    companion object {
        private const val IMAGE_FILE_NAME = "00000.jpg"
    }

    override suspend fun invoke(path: String): ByteArray? {
        return fileReadingService.readFile("$path/$IMAGE_FILE_NAME")?.readBytes()
    }
}
