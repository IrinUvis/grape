package uvis.irin.grape.core.android.service.file

import java.io.File

interface FileSharingService {

    suspend fun shareFile(file: File, mimeType: String)
}
