package uvis.irin.grape.soundlist.domain.usecase

import java.io.File

interface FetchLocalSoundFileForPathUseCase {

    suspend operator fun invoke(path: String): File?
}
