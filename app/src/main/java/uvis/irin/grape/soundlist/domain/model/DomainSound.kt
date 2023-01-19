package uvis.irin.grape.soundlist.domain.model

import uvis.irin.grape.soundlist.data.model.Sound

data class DomainSound(
    val name: String,
    val path: String,
)

fun Sound.toDomainSound() = DomainSound(
    name = this.name,
    path = this.path,
)
