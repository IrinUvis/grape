package uvis.irin.grape.core.android.service.file

import java.io.File

interface FileReadingService {

    suspend fun readFile(path: String): File?

    suspend fun readAllDirectoriesInDirectory(path: String): List<File>

    suspend fun readAllFilesInDirectory(path: String): List<File>

    suspend fun fileExists(path: String): Boolean
}
