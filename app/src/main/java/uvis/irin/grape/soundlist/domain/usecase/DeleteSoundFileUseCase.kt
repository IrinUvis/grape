package uvis.irin.grape.soundlist.domain.usecase

interface DeleteSoundFileUseCase {

    suspend operator fun invoke(path: String)
}
