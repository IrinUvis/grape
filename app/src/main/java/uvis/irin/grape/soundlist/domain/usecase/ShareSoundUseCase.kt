package uvis.irin.grape.soundlist.domain.usecase

import java.io.File

interface ShareSoundUseCase {

    suspend operator fun invoke(soundFile: File)
}
