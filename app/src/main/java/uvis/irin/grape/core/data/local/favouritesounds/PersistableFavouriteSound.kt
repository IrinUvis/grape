package uvis.irin.grape.core.data.local.favouritesounds

import androidx.room.Entity
import androidx.room.PrimaryKey
import uvis.irin.grape.soundlist.domain.model.FavouriteSound

@Entity(tableName = "favourite_sound")
class PersistableFavouriteSound(
    @PrimaryKey
    val completePath: String
)

fun PersistableFavouriteSound.toFavouriteSound() = FavouriteSound(
    completePath = completePath
)

fun FavouriteSound.toPersistableFavouriteSound() = PersistableFavouriteSound(
    completePath = completePath
)