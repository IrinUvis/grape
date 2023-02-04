package uvis.irin.grape.soundlist.domain.usecase.impl

import java.io.File
import javax.inject.Inject
import uvis.irin.grape.core.android.service.file.FileSharingService
import uvis.irin.grape.soundlist.domain.usecase.ShareSoundUseCase

class ProdShareSoundUseCase @Inject constructor(
    private val fileSharingService: FileSharingService,
) : ShareSoundUseCase {
    companion object {
        private const val MP3_MIME_TYPE = "audio/mp3"
    }

    override suspend fun invoke(soundFile: File) {
        fileSharingService.shareFile(
            soundFile,
            mimeType = MP3_MIME_TYPE
        )
    }
}
