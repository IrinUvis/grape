package uvis.irin.grape.soundlist.ui

import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory
import uvis.irin.grape.soundlist.domain.model.Sound

data class SoundListViewState(
    val showLoading: Boolean = true,
    val categories: List<ResourceSoundCategory> = emptyList(),
    val selectedCategory: ResourceSoundCategory? = null,
    val subcategories: List<ResourceSoundCategory>? = null,
    val selectedSubcategory: ResourceSoundCategory? = null,
    val sounds: List<Sound> = emptyList(),
    val errorMessage: String? = null,
)
