package uvis.irin.grape.soundlist.domain.model

import java.text.Collator
import java.util.Locale

data class ResourceSound(
    override val name: String,
    val category: ResourceSoundCategory,
    val relativeAssetPath: String,
) : Sound, Comparable<ResourceSound> {
    val completePath: String
        get() = "${this.category.assetsPath}/${this.relativeAssetPath}"

    override fun compareTo(other: ResourceSound): Int {
        val coll = Collator.getInstance(Locale("pl", "PL"))
        coll.strength = Collator.PRIMARY
        return coll.compare(
            name.lowercase(Locale("pl", "PL")),
            other.name.lowercase(Locale("pl", "PL"))
        )
    }
}
