package uvis.irin.grape.soundlist.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.core.android.service.file.FileDeletingService
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.soundlist.domain.usecase.DeleteSoundFileUseCase

class ProdDeleteSoundFileUseCase @Inject constructor(
    private val fileReadingService: FileReadingService,
    private val fileDeletingService: FileDeletingService,
) : DeleteSoundFileUseCase {

    override suspend fun invoke(path: String) {
        val file = fileReadingService.readFile(path)

        file?.let { fileDeletingService.deleteFile(file) }
    }
}
