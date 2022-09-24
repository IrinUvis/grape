package uvis.irin.grape.soundlist.domain.model

data class ResourceSoundCategory(
    override val name: String,
    override val subcategories: List<ResourceSoundCategory>?,
    val assetsPath: String
) : SoundCategory
