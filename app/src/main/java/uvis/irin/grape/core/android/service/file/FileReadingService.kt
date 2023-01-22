package uvis.irin.grape.core.android.service.file

import java.io.File

interface FileReadingService {

    fun readFile(path: String): File?

    fun readAllFilesInDirectory(path: String): List<File>

    fun fileExists(path: String): Boolean
}
