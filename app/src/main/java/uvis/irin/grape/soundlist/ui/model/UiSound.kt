package uvis.irin.grape.soundlist.ui.model

import java.io.File
import uvis.irin.grape.soundlist.domain.model.DomainSound

data class UiSound(
    val fileName: String,
    val path: String,
    val isFavourite: Boolean,
    val downloadState: DownloadState,
    val localFile: File?
) {
    val name get() = fileName.substringBeforeLast('.')
}

fun DomainSound.toUiSound() = UiSound(
    fileName = this.fileName,
    path = this.path,
    isFavourite = false,
    downloadState = DownloadState.NotDownloaded,
    localFile = null,
)

fun UiSound.toDomainSound() = DomainSound(
    fileName = this.fileName,
    path = this.path,
)
