package uvis.irin.grape.categories.data.repository

import uvis.irin.grape.core.data.DataResult
import uvis.irin.grape.categories.data.model.Category

interface CategoryRepository {

    suspend fun fetchCategoriesForPath(path: String): DataResult<List<Category>>
}
