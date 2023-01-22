package uvis.irin.grape.core.android.service.file.impl

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import uvis.irin.grape.core.android.service.file.FileDeletingService

class ProdFileDeletingService @Inject constructor(
    @ApplicationContext private val context: Context,
) : FileDeletingService {

    override fun deleteFile(file: File) {
        file.delete()
    }

    override fun clearDirectory(directory: File) {
        val tempFiles = directory.listFiles() ?: emptyArray()
        for (tempFile in tempFiles) {
            if (!tempFile.isDirectory)
                tempFile.delete()
        }
    }

    override fun clearCache() {
        clearDirectory(context.cacheDir)
    }
}
