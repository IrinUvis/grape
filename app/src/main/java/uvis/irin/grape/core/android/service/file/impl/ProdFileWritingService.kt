package uvis.irin.grape.core.android.service.file.impl

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uvis.irin.grape.core.android.service.file.FileWritingService
import uvis.irin.grape.core.di.IoDispatcher

class ProdFileWritingService @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FileWritingService {

    override suspend fun writeFileToInternalStorage(path: String, bytes: ByteArray): File {
        return withContext(ioDispatcher) {
            val splitPathParts = path.split('/').drop(1)
            val fileName = splitPathParts.last()
            val subDirs = splitPathParts.dropLast(1)

            var dir = context.filesDir

            for (subDir in subDirs) {
                dir = File(dir, subDir)
                if (!dir.exists()) {
                    dir.mkdir()
                }
            }

            val soundFile = File(dir, fileName)
            val fos = FileOutputStream(soundFile)
            fos.write(bytes)
            fos.close()

            soundFile
        }
    }

    override suspend fun writeFileToCachedStorage(name: String, bytes: ByteArray): File {
        return withContext(ioDispatcher) {
            val file = File(context.cacheDir, name)

            val fos = FileOutputStream(file)
            fos.write(bytes)
            fos.close()

            file
        }
    }
}
