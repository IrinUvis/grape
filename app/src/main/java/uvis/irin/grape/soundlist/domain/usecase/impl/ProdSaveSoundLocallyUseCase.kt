package uvis.irin.grape.soundlist.domain.usecase.impl

import java.io.File
import javax.inject.Inject
import uvis.irin.grape.core.android.service.file.FileWritingService
import uvis.irin.grape.soundlist.domain.usecase.SaveSoundLocallyUseCase

class ProdSaveSoundLocallyUseCase @Inject constructor(
    private val fileWritingService: FileWritingService,
) : SaveSoundLocallyUseCase {

    override suspend fun invoke(path: String, bytes: ByteArray): File {
        return fileWritingService.writeFileToInternalStorage(path, bytes)
    }
}
