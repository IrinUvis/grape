package uvis.irin.grape.soundlist.domain.model

import uvis.irin.grape.soundlist.data.model.FavouriteSoundPath

data class DomainFavouriteSoundPath(
    val path: String
)

fun FavouriteSoundPath.toDomainFavouriteSoundPath() = DomainFavouriteSoundPath(path = path)
