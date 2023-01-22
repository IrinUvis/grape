package uvis.irin.grape.soundlist.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import uvis.irin.grape.R
import uvis.irin.grape.soundlist.ui.model.DownloadState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DownloadButton(
    modifier: Modifier = Modifier,
    downloadState: DownloadState,
    onClick: () -> Unit,
    contentDescription: String,
) {
    val downloadIcon = when (downloadState) {
        DownloadState.NotDownloaded -> painterResource(R.drawable.outline_download_for_offline_24)
        DownloadState.Downloading -> painterResource(R.drawable.baseline_downloading_24)
        DownloadState.Downloaded -> painterResource(R.drawable.baseline_download_done_24)
        DownloadState.ErrorDownloading -> painterResource(R.drawable.baseline_error_outline_24)
    }

    IconButton(
        modifier = modifier,
        enabled = downloadState != DownloadState.Downloading,
        onClick = onClick,
    ) {
        AnimatedContent(targetState = downloadIcon) {icon ->
            Icon(
                painter = icon,
                contentDescription = contentDescription,
            )
        }
    }
}
