package uvis.irin.grape.core.data.local.favouritesounds

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouriteSoundDao {
    @Query("SELECT * FROM favourite_sound")
    fun getAll(): List<PersistableFavouriteSound>

    @Insert
    suspend fun insertFavouriteSound(favouriteSound: PersistableFavouriteSound)

    @Delete
    suspend fun deleteFavouriteSound(favouriteSound: PersistableFavouriteSound)
}