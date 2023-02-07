package uvis.irin.grape.categories.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.categories.domain.model.DomainCategory
import uvis.irin.grape.categories.domain.usecase.FetchLocalCategoriesForPathUseCase
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.core.comparator.DirectoryAndFileNameComparator

class ProdFetchLocalCategoriesForPathUseCase @Inject constructor(
    private val fileReadingService: FileReadingService,
) : FetchLocalCategoriesForPathUseCase {

    override suspend fun invoke(path: String): List<DomainCategory> {
        val categoryFiles = fileReadingService.readAllDirectoriesInDirectory(path)

        val areFinalCategories = categoryFiles.map { isFinalCategory("$path/${it.name}") }

        return categoryFiles.sortedWith { category1, category2 ->
            DirectoryAndFileNameComparator().compare(category1.name, category2.name)
        }.mapIndexed { index, file ->
            DomainCategory(
                path = "$path/${file.name}",
                isFinalCategory = areFinalCategories[index]
            )
        }
    }

    private suspend fun isFinalCategory(path: String): Boolean {
        return fileReadingService.readAllDirectoriesInDirectory(path).isEmpty()
    }
}
