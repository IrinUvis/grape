package uvis.irin.grape.soundlist.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.soundlist.data.repository.FavouriteSoundRepository
import uvis.irin.grape.soundlist.domain.usecase.DeleteFavouriteSoundsNotPresentInListUseCase
import uvis.irin.grape.soundlist.domain.model.DomainFavouriteSoundPath
import uvis.irin.grape.soundlist.domain.model.DomainSound

class ProdDeleteFavouriteSoundsNotPresentInListUseCase @Inject constructor(
    private val favouriteSoundRepository: FavouriteSoundRepository
) : DeleteFavouriteSoundsNotPresentInListUseCase {
    override suspend fun invoke(
        sounds: List<DomainSound>,
        favouriteSoundPaths: List<DomainFavouriteSoundPath>
    ) {
        val soundsPaths = sounds.map { it.path }

        val favouriteSoundsToDelete = favouriteSoundPaths.filter { !soundsPaths.contains(it.path) }

        for (favouriteSoundToDelete in favouriteSoundsToDelete) {
            favouriteSoundRepository.deleteFavouriteSound(favouriteSoundToDelete.path)
        }
    }
}
