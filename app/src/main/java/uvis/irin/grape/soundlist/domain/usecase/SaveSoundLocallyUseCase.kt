package uvis.irin.grape.soundlist.domain.usecase

import java.io.File

interface SaveSoundLocallyUseCase {

    suspend operator fun invoke(path: String, bytes: ByteArray): File
}
