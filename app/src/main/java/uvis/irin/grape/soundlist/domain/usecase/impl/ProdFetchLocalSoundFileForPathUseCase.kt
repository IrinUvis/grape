package uvis.irin.grape.soundlist.domain.usecase.impl

import java.io.File
import javax.inject.Inject
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.soundlist.domain.usecase.FetchLocalSoundFileForPathUseCase

class ProdFetchLocalSoundFileForPathUseCase @Inject constructor(
    private val fileReadingService: FileReadingService,
) : FetchLocalSoundFileForPathUseCase {

    override suspend fun invoke(path: String): File? {
        return fileReadingService.readFile(path)
    }
}
