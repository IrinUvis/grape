package uvis.irin.grape.soundlist.domain.usecase

import java.io.File

interface CreateCacheSoundFileUseCase {

    suspend operator fun invoke(bytes: ByteArray): File
}
