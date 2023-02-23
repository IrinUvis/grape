package uvis.irin.grape.soundlist.domain.usecase

interface GetFileExistsUseCase {

    suspend operator fun invoke(path: String): Boolean
}
