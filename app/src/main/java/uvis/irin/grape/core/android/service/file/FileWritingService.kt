package uvis.irin.grape.core.android.service.file

import java.io.File

interface FileWritingService {

    suspend fun writeFileToInternalStorage(path: String, bytes: ByteArray): File

    suspend fun writeFileToCachedStorage(name: String, bytes: ByteArray): File
}
