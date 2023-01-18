package uvis.irin.grape.soundlist.domain.model

import uvis.irin.grape.soundlist.data.model.Sound

data class DomainSound(
    val name: String,
)

fun Sound.toDomainSound() = DomainSound(
    name = this.name,
)
