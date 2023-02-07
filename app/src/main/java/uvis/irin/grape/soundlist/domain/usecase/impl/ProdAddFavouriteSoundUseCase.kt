package uvis.irin.grape.soundlist.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.soundlist.data.repository.FavouriteSoundRepository
import uvis.irin.grape.soundlist.domain.usecase.AddFavouriteSoundUseCase

class ProdAddFavouriteSoundUseCase @Inject constructor(
    private val favouriteSoundRepository: FavouriteSoundRepository,
) : AddFavouriteSoundUseCase {

    override suspend fun invoke(path: String) {
        favouriteSoundRepository.addFavouriteSound(path)
    }
}
