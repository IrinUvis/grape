package uvis.irin.grape.soundlist.ui.model

import java.io.File
import uvis.irin.grape.BuildConfig
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

fun File.toUiSound() = UiSound(
    fileName = this.name,
    path = this.path.substringAfter(
        BuildConfig.APPLICATION_ID + "/files"),
    isFavourite = false,
    downloadState = DownloadState.Downloaded,
    localFile = this,
)

fun DomainSound.toUiSound() = UiSound(
    fileName = this.name,
    path = this.path,
    isFavourite = false,
    downloadState = DownloadState.NotDownloaded,
    localFile = null,
)
