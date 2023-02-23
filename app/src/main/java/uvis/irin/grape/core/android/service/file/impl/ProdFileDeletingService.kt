package uvis.irin.grape.core.android.service.file.impl

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uvis.irin.grape.core.android.service.file.FileDeletingService
import uvis.irin.grape.core.di.IoDispatcher

class ProdFileDeletingService @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FileDeletingService {

    override suspend fun deleteFile(file: File) {
        withContext(ioDispatcher) { file.delete() }
    }

    override suspend fun clearDirectory(directory: File) {
        withContext(ioDispatcher) {
            val tempFiles = directory.listFiles() ?: emptyArray()
            for (tempFile in tempFiles) {
                if (tempFile.isFile) tempFile.delete()
                if (tempFile.isDirectory) clearDirectory(tempFile)
            }
        }
    }

    override suspend fun clearCache() {
        withContext(ioDispatcher) {
            clearDirectory(context.cacheDir)
        }
    }
}
