package uvis.irin.grape.categories.domain.model

import uvis.irin.grape.categories.data.model.Category

data class DomainCategory(
    val path: String,
    val isFinalCategory: Boolean,
)

fun Category.toDomainCategory() = DomainCategory(
    path = this.path,
    isFinalCategory = this.isFinalCategory,
)
