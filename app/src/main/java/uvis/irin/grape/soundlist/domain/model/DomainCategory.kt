package uvis.irin.grape.soundlist.domain.model

import uvis.irin.grape.soundlist.data.model.Category

data class DomainCategory(
    val name: String,
    val absolutePath: String
)

fun Category.toDomainCategory() = DomainCategory(
    name = this.name,
    absolutePath = this.absolutePath,
)
