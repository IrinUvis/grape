package uvis.irin.grape.soundlist.ui.model

import uvis.irin.grape.soundlist.domain.model.DomainSound

data class UiSound(
    val name: String,
    val favourite: Boolean,
)

fun DomainSound.toUiSound() = UiSound(
    name = this.name,
    favourite = false,
)
