package uvis.irin.grape.categories.ui.model

import android.graphics.Bitmap
import uvis.irin.grape.categories.domain.model.DomainCategory
import uvis.irin.grape.core.extension.capitalize

data class UiCategory(
    val path: String,
    val bitmap: Bitmap,
) {
    val name: String
        get() = categoryPathToName(path)
}

fun DomainCategory.toUiCategory(bitmap: Bitmap) = UiCategory(
    path = this.path,
    bitmap = bitmap,
)

private fun categoryPathToName(path: String): String {
    return path
        .substringAfterLast('/')
        .substringAfterLast('_')
        .capitalize()
}
