package uvis.irin.grape.categories.domain.model

import uvis.irin.grape.categories.data.model.Category

data class DomainCategory(
    val name: String,
    val path: String
)

fun Category.toDomainCategory() = DomainCategory(
    name = this.name,
    path = this.path,
)
