package uvis.irin.grape.soundlist.domain.model

import uvis.irin.grape.soundlist.data.model.Sound

data class DomainSound(
    val fileName: String,
    val path: String,
)

fun Sound.toDomainSound() = DomainSound(
    fileName = this.fileName,
    path = this.path,
)
