package uvis.irin.grape.soundlist.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.core.comparator.DirectoryAndFileNameComparator
import uvis.irin.grape.soundlist.domain.model.DomainSound
import uvis.irin.grape.soundlist.domain.usecase.FetchLocalSoundsForPathUseCase

class ProdFetchLocalSoundsForPathUseCase @Inject constructor(
    private val fileReadingService: FileReadingService,
) : FetchLocalSoundsForPathUseCase {

    override suspend fun invoke(path: String): List<DomainSound> {
        val soundFiles = fileReadingService.readAllFilesInDirectory(path)

        return soundFiles.filter { it.extension == "mp3" }.sortedWith { soundFile1, soundFile2 ->
            DirectoryAndFileNameComparator().compare(soundFile1.name, soundFile2.name)
        }.map { file ->
            DomainSound(
                fileName = file.name,
                path = "$path/${file.name}"
            )
        }
    }
}
