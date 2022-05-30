package uvis.irin.grape.soundlist.ui

import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory

data class SoundListViewState(
    val showLoading: Boolean = true,
    val categories: List<SoundCategory> = emptyList(),
    val selectedCategory: SoundCategory? = null,
    val sounds: List<Sound> = emptyList(),
    val errorMessage: String? = null,
)
