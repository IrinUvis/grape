package uvis.irin.grape.core.data.local.favouritesounds

import androidx.room.Entity
import androidx.room.PrimaryKey
import uvis.irin.grape.core.capitalize
import uvis.irin.grape.core.trimFileExtension
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.ResourceSoundCategory

@Entity(tableName = "favourite_sound")
class PersistableFavouriteSound(
    @PrimaryKey
    val completePath: String
)

fun PersistableFavouriteSound.toResourceSound(): ResourceSound {
    val categoryAssetsPath = completePath.split('/').dropLast(1).joinToString("/")
    val relevantPath = completePath.split('/').drop(1)
    val categoryName = relevantPath.dropLast(1).last().split('_').last().capitalize()
    val relativeAssetPath = relevantPath.last()
    val name = relativeAssetPath.split('_').last().capitalize().trimFileExtension()

    return ResourceSound(
        name = name,
        category = ResourceSoundCategory(
            name = categoryName,
            subcategories = null,
            assetsPath = categoryAssetsPath
        ),
        relativeAssetPath = relativeAssetPath
    )
}

fun ResourceSound.toPersistableFavouriteSound() = PersistableFavouriteSound(
    completePath = completePath
)
