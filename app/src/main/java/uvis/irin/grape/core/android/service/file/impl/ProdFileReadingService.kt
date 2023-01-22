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

        val file = File(dir, fileName)

        return if (file.exists()) file else null
    }

    override fun readAllFilesInDirectory(path: String): List<File> {
        val splitPathParts = path.split('/')

        var dir = context.filesDir

        for (subDir in splitPathParts) {
            dir = File(dir, subDir)
            if (!dir.exists()) {
                return emptyList()
            }
        }

        val children = dir.listFiles() ?: emptyArray()

        return children.filter { it.isFile }
    }

    override fun fileExists(path: String): Boolean {
        val file = readFile(path)

        return file != null
    }
}
