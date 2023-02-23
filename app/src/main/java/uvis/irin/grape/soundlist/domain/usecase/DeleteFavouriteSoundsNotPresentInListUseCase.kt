package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.soundlist.domain.model.DomainFavouriteSoundPath
import uvis.irin.grape.soundlist.domain.model.DomainSound

interface DeleteFavouriteSoundsNotPresentInListUseCase {

    suspend operator fun invoke(
        sounds: List<DomainSound>,
        favouriteSoundPaths: List<DomainFavouriteSoundPath>
    )
}
