package uvis.irin.grape.soundlist.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.soundlist.domain.usecase.GetFileExistsUseCase

class ProdGetFileExistsUseCase @Inject constructor(
    private val fileReadingService: FileReadingService,
) : GetFileExistsUseCase {

    override suspend fun invoke(path: String): Boolean {
        return fileReadingService.fileExists(path)
    }
}
