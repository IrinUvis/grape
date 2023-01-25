package uvis.irin.grape.categories.domain.model

import uvis.irin.grape.categories.data.model.Category

data class DomainCategory(
    val name: String,
    val absolutePath: String
)

fun Category.toDomainCategory() = DomainCategory(
    name = this.name,
    absolutePath = this.path,
)
