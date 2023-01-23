package uvis.irin.grape.soundlist.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uvis.irin.grape.R
import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.core.ui.components.AppBarTitle
import uvis.irin.grape.core.ui.components.NavigateBackButton
import uvis.irin.grape.soundlist.ui.model.DownloadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundListTopAppBar(
    modifier: Modifier = Modifier,
    category: UiCategory,
    soundsLoaded: Boolean,
    soundsDownloadState: DownloadState,
    onNavigationIconClicked: () -> Unit,
    onDownloadForOfflineIconClicked: () -> Unit,
    onSettingsIconClicked: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    MediumTopAppBar(
        modifier = modifier,
        title = { AppBarTitle(text = category.name) },
        navigationIcon = {
            NavigateBackButton(
                onClick = onNavigationIconClicked,
                contentDescription = stringResource(R.string.navigationIconContentDescription),
            )
        },
        actions = {
            AnimatedVisibility(visible = soundsLoaded) {
                DownloadButton(
                    downloadState = soundsDownloadState,
                    onClick = onDownloadForOfflineIconClicked,
                    contentDescription = stringResource(R.string.downloadCategorySoundsButtonContentDescription),
                )
            }

            SettingsButton(
                onClick = onSettingsIconClicked,
                contentDescription = stringResource(R.string.downloadCategorySoundsButtonContentDescription),
            )
        },
        scrollBehavior = scrollBehavior,
    )
}
