package uvis.irin.grape.core.android.service.file

interface FileWritingService {

    fun writeFile(path: String, bytes: ByteArray)
}
