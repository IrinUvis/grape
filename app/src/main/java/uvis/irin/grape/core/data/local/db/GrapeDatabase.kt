package uvis.irin.grape.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import uvis.irin.grape.core.data.local.favouritesounds.FavouriteSoundDao
import uvis.irin.grape.core.data.local.favouritesounds.PersistableFavouriteSound

@Database(
    entities = [PersistableFavouriteSound::class],
    version = 1
)
abstract class GrapeDatabase : RoomDatabase() {
    abstract fun favouriteSoundsDao(): FavouriteSoundDao
}
