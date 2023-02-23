package uvis.irin.grape.soundlist.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.soundlist.data.repository.FavouriteSoundRepository
import uvis.irin.grape.soundlist.domain.usecase.DeleteFavouriteSoundUseCase

class ProdDeleteFavouriteSoundUseCase @Inject constructor(
    private val favouriteSoundRepository: FavouriteSoundRepository
) : DeleteFavouriteSoundUseCase {

    override suspend fun invoke(path: String) {
        favouriteSoundRepository.deleteFavouriteSound(path)
    }
}
