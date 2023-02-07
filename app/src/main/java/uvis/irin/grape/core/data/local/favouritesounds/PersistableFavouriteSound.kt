package uvis.irin.grape.core.data.local.favouritesounds

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_sound")
class PersistableFavouriteSound(
    @PrimaryKey
    val path: String,
)

