package uvis.irin.grape.soundlist.domain.model

data class SoundCategory(
    val name: String,
    val subcategories: List<SoundCategory>?,
    val assetsPath: String
)
