package uvis.irin.grape.core.android.service.file

import java.io.File

interface FileDeletingService {

    suspend fun deleteFile(file: File)

    suspend fun clearDirectory(directory: File)

    suspend fun clearCache()
}
