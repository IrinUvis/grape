package uvis.irin.grape.soundlist.data.repository

import uvis.irin.grape.soundlist.data.model.FavouriteSoundPath

interface FavouriteSoundRepository {

    suspend fun fetchFavouriteSoundsPaths(path: String): List<FavouriteSoundPath>

    suspend fun addFavouriteSound(path: String)

    suspend fun deleteFavouriteSound(path: String)
}
