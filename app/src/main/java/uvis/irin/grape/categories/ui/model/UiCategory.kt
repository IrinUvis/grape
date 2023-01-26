package uvis.irin.grape.categories.ui.model

import android.graphics.Bitmap
import uvis.irin.grape.categories.domain.model.DomainCategory

data class UiCategory(
    val path: String,
    val isFirstCategory: Boolean,
    val isFinalCategory: Boolean,
    val bitmap: Bitmap?,
) {
    val name: String
        get() = categoryPathToName(path)
}

fun DomainCategory.toUiCategory(
    isFirstCategory: Boolean = false,
    bitmap: Bitmap? = null,
) = UiCategory(
    path = this.path,
    isFirstCategory = isFirstCategory,
    isFinalCategory = this.isFinalCategory,
    bitmap = bitmap,
)

fun UiCategory.toDomainCategory() = DomainCategory(
    path = this.path,
    isFinalCategory = this.isFinalCategory,
)

private fun categoryPathToName(path: String): String {
    return path
        .substringAfterLast('/')
        .substringAfterLast('_')
}
