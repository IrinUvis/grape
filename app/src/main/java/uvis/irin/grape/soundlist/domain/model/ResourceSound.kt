package uvis.irin.grape.soundlist.domain.model

data class ResourceSound(
    override val name: String,
    override val category: SoundCategory,
    val relativeAssetPath: String
) : Sound
