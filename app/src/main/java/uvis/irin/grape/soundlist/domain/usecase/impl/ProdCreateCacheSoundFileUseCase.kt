package uvis.irin.grape.soundlist.domain.usecase.impl

import java.io.File
import javax.inject.Inject
import uvis.irin.grape.core.android.service.file.FileWritingService
import uvis.irin.grape.soundlist.domain.usecase.CreateCacheSoundFileUseCase

class ProdCreateCacheSoundFileUseCase @Inject constructor(
    private val fileWritingService: FileWritingService,
) : CreateCacheSoundFileUseCase {
    companion object {
        private const val FILE_NAME = "sound.mp3"
    }

    override suspend fun invoke(bytes: ByteArray): File {
        return fileWritingService.writeFileToCachedStorage(FILE_NAME, bytes)
    }
}
