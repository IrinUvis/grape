package uvis.irin.grape.core.android.service.file.impl

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uvis.irin.grape.core.android.service.file.FileReadingService
import uvis.irin.grape.core.di.IoDispatcher

class ProdFileReadingService @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FileReadingService {

    override suspend fun readFile(path: String): File? {
        return withContext(ioDispatcher) {
            val splitPathParts = path.split('/').drop(1)
            val fileName = splitPathParts.last()
            val subDirs = splitPathParts.dropLast(1)

            var dir = context.filesDir

            for (subDir in subDirs) {
                dir = File(dir, subDir)
                if (!dir.exists()) {
                    return@withContext null
                }
            }

            val file = File(dir, fileName)

            if (file.exists()) file else null
        }
    }

    override suspend fun readAllDirectoriesInDirectory(path: String): List<File> {
        return withContext(ioDispatcher) {
            val files = readAllFileObjectsByPath(path)

            files.filter { it.isDirectory }
        }
    }

    override suspend fun readAllFilesInDirectory(path: String): List<File> {
        return withContext(ioDispatcher) {
            val files = readAllFileObjectsByPath(path)

            files.filter { it.isFile }
        }
    }

    override suspend fun fileExists(path: String): Boolean {
        return withContext(ioDispatcher) {
            val file = readFile(path)

             file != null
        }
    }

    private fun readAllFileObjectsByPath(path: String): List<File> {
        val splitPathParts = path.split('/')

        var dir = context.filesDir

        for (subDir in splitPathParts) {
            dir = File(dir, subDir)
            if (!dir.exists()) {
                return emptyList()
            }
        }

        val children = dir.listFiles() ?: emptyArray()

        return children.asList()
    }
}
