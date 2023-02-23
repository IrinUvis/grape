package uvis.irin.grape.soundlist.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.soundlist.data.repository.FavouriteSoundRepository
import uvis.irin.grape.soundlist.domain.model.DomainFavouriteSoundPath
import uvis.irin.grape.soundlist.domain.model.toDomainFavouriteSoundPath
import uvis.irin.grape.soundlist.domain.usecase.FetchFavouriteSoundsUseCase

class ProdFetchFavouriteSoundsUseCase @Inject constructor(
    private val favouriteSoundRepository: FavouriteSoundRepository
) : FetchFavouriteSoundsUseCase {

    override suspend fun invoke(path: String): List<DomainFavouriteSoundPath> {
        return favouriteSoundRepository.fetchFavouriteSoundsPaths(path)
            .map { it.toDomainFavouriteSoundPath() }
    }
}
