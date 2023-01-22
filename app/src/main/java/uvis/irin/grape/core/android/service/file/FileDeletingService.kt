package uvis.irin.grape.core.android.service.file

import java.io.File

interface FileDeletingService {

    fun deleteFile(file: File)

    fun clearDirectory(directory: File)

    fun clearCache()
}
