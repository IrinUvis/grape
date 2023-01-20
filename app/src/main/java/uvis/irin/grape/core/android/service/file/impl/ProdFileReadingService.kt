package uvis.irin.grape.core.android.service.file.impl

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import uvis.irin.grape.core.android.service.file.FileReadingService

class ProdFileReadingService @Inject constructor(
    @ApplicationContext private val context: Context,
) : FileReadingService {

    override fun readFile(path: String): File? {
        val splitPathParts = path.split('/').drop(1)
        val fileName = splitPathParts.last()
        val subDirs = splitPathParts.dropLast(1)

        var dir = context.filesDir

        for (subDir in subDirs) {
            dir = File(dir, subDir)
            if (!dir.exists()) {
                return null
            }
        }

        val soundFile = File(dir, fileName)

        return if (soundFile.exists()) soundFile else null
    }
}
