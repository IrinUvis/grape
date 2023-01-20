package uvis.irin.grape.core.provider.file

import java.io.File

interface FileSharingService {

    fun shareFile(file: File, mimeType: String)
}