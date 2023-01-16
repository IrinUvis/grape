package uvis.irin.grape.soundlist.data.repository

import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.soundlist.data.model.Category

interface CategoryRepository {

    suspend fun fetchCategoriesForPath(path: String): DataResult<List<Category>>
}