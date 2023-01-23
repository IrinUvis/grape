package uvis.irin.grape.categories.ui.model

import uvis.irin.grape.core.extension.capitalize
import uvis.irin.grape.soundlist.domain.model.DomainCategory

data class UiCategory(
    val path: String,
) {
    val name: String
        get() = categoryPathToName(path)
}

fun DomainCategory.toUiCategory() = UiCategory(
    path = this.absolutePath,
)

private fun categoryPathToName(path: String): String {
    return path
        .substringAfterLast('/')
        .substringAfterLast('_')
        .capitalize()
}
