package uvis.irin.grape.categories.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.categories.domain.usecase.SaveImageByteArrayLocallyUseCase
import uvis.irin.grape.core.android.service.file.FileWritingService

class ProdSaveImageByteArrayLocallyUseCase @Inject constructor(
    private val fileWritingService: FileWritingService,
) : SaveImageByteArrayLocallyUseCase {
    companion object {
        private const val IMAGE_FILE_NAME = "00000.jpg"
    }

    override suspend fun invoke(path: String, byteArray: ByteArray) {
        fileWritingService.writeFileToInternalStorage(
            path = "$path/$IMAGE_FILE_NAME",
            bytes = byteArray,
        )
    }
}
