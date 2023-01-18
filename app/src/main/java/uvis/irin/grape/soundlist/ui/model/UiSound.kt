package uvis.irin.grape.soundlist.ui.model

import uvis.irin.grape.soundlist.domain.model.DomainSound

data class UiSound(
    val fileName: String,
    val isFavourite: Boolean,
    val bytes: List<Byte>? = null,
) {
    val name get() = fileName.substringBeforeLast('.')
}

fun DomainSound.toUiSound() = UiSound(
    fileName = this.name,
    isFavourite = false,
)
