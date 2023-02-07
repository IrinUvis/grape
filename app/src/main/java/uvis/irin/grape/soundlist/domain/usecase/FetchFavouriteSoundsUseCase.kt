package uvis.irin.grape.soundlist.domain.usecase

import uvis.irin.grape.soundlist.domain.model.DomainFavouriteSoundPath

interface FetchFavouriteSoundsUseCase {

    suspend operator fun invoke(path: String): List<DomainFavouriteSoundPath>
}
