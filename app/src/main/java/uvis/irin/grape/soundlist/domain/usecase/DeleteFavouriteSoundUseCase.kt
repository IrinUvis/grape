package uvis.irin.grape.soundlist.domain.usecase

interface DeleteFavouriteSoundUseCase {

    suspend operator fun invoke(path: String)
}
