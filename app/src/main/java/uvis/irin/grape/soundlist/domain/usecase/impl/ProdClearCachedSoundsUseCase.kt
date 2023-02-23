package uvis.irin.grape.soundlist.domain.usecase.impl

import javax.inject.Inject
import uvis.irin.grape.core.android.service.file.FileDeletingService
import uvis.irin.grape.soundlist.domain.usecase.ClearCachedSoundsUseCase

class ProdClearCachedSoundsUseCase @Inject constructor(
    private val fileDeletingService: FileDeletingService,
) : ClearCachedSoundsUseCase {

    override suspend fun invoke() {
        fileDeletingService.clearCache()
    }
}
