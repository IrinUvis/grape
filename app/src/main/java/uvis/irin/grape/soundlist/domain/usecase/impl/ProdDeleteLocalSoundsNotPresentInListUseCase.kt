package uvis.irin.grape.soundlist.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.core.android.service.file.FileDeletingService
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.soundlist.domain.model.DomainSound
import uvis.irin.grape.soundlist.domain.usecase.DeleteLocalSoundsNotPresentInListUseCase

class ProdDeleteLocalSoundsNotPresentInListUseCase @Inject constructor(
    private val fileReadingService: FileReadingService,
    private val fileDeletingService: FileDeletingService,
) : DeleteLocalSoundsNotPresentInListUseCase {

    override suspend fun invoke(path: String, soundList: List<DomainSound>) {
        val soundFileNames = soundList.map { it.fileName }

        val localSoundFiles = fileReadingService.readAllFilesInDirectory(path)

        val soundsToDelete = localSoundFiles.filter { !soundFileNames.contains(it.name) }

        for (soundToDelete in soundsToDelete) {
            fileDeletingService.deleteFile(soundToDelete)
        }
    }
}
