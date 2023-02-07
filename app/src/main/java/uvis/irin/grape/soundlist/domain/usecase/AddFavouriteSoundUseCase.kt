package uvis.irin.grape.soundlist.domain.usecase

interface AddFavouriteSoundUseCase {

    suspend operator fun invoke(path: String)
}
