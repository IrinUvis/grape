package uvis.irin.grape.categories.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.categories.domain.model.DomainCategory
import uvis.irin.grape.categories.domain.usecase.DeleteLocalCategoriesNotPresentInListUseCase
import uvis.irin.grape.core.android.service.file.FileDeletingService
import uvis.irin.grape.core.android.service.file.FileReadingService

class ProdDeleteLocalCategoriesNotPresentInListUseCase @Inject constructor(
    private val fileReadingService: FileReadingService,
    private val fileDeletingService: FileDeletingService,
) : DeleteLocalCategoriesNotPresentInListUseCase {

    override suspend fun invoke(path: String, categoryList: List<DomainCategory>) {
        val directoryNames = categoryList.map { it.name }

        val localDirectories = fileReadingService.readAllDirectoriesInDirectory(path)

        val directoriesToDelete = localDirectories.filter { !directoryNames.contains(it.name) }

        for (directoryToDelete in directoriesToDelete) {
            fileDeletingService.clearDirectory(directoryToDelete)
            fileDeletingService.deleteFile(directoryToDelete)
        }
    }
}
