package uvis.irin.grape.soundlist.ui

import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory

data class SoundListViewState(
    val showLoading: Boolean = true,
    val categories: List<ResourceSoundCategory> = emptyList(),
    val selectedCategory: ResourceSoundCategory? = null,
    val subcategories: List<ResourceSoundCategory>? = null,
    val selectedSubcategory: ResourceSoundCategory? = null,
    val sounds: List<ResourceSound> = emptyList(),
    val favouriteSounds: List<ResourceSound> = emptyList(),
    val displayOnlyFavourites: Boolean = false,
    val searchQuery: String = "",
    val errorMessage: String? = null,
)
