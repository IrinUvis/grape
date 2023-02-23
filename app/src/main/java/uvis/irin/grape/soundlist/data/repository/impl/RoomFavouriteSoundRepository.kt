package uvis.irin.grape.soundlist.data.repository.impl

import javax.inject.Inject
import uvis.irin.grape.core.data.local.favouritesounds.FavouriteSoundDao
import uvis.irin.grape.core.data.local.favouritesounds.PersistableFavouriteSound
import uvis.irin.grape.soundlist.data.model.FavouriteSoundPath
import uvis.irin.grape.soundlist.data.repository.FavouriteSoundRepository

class RoomFavouriteSoundRepository @Inject constructor(
    private val favouriteSoundDao: FavouriteSoundDao,
) : FavouriteSoundRepository {

    override suspend fun fetchFavouriteSoundsPaths(path: String): List<FavouriteSoundPath> {
        return favouriteSoundDao.getAllByPath(path).map { it.toFavouriteSoundPath() }
    }

    override suspend fun addFavouriteSound(path: String) {
        favouriteSoundDao.insertFavouriteSound(path.toPersistableFavouriteSound())
    }

    override suspend fun deleteFavouriteSound(path: String) {
        favouriteSoundDao.deleteFavouriteSound(path.toPersistableFavouriteSound())
    }
}

private fun String.toPersistableFavouriteSound() = PersistableFavouriteSound(path = this)

private fun PersistableFavouriteSound.toFavouriteSoundPath() = FavouriteSoundPath(path = path)
