package uvis.irin.grape.soundlist.domain.model

data class ResourceSound(
    override val name: String,
    val category: ResourceSoundCategory,
    val relativeAssetPath: String,
) : Sound {
    val completePath: String
        get() = "${this.category.assetsPath}/${this.relativeAssetPath}"
}
