package uvis.irin.grape.core.android.service.file

import java.io.File

interface FileWritingService {

    fun writeFileToInternalStorage(path: String, bytes: ByteArray): File

    fun writeFileToCachedStorage(name: String, bytes: ByteArray): File
}
