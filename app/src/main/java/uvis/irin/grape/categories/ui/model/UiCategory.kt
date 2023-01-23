package uvis.irin.grape.categories.ui.model

import uvis.irin.grape.soundlist.domain.model.DomainCategory

data class UiCategory(
    val name: String,
    val absolutePath: String,
)

fun DomainCategory.toUiCategory() = UiCategory(
    name = this.name,
    absolutePath = this.absolutePath,
)
