package uvis.irin.grape.soundlist.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uvis.irin.grape.R
import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.core.ui.components.AppBarTitle
import uvis.irin.grape.core.ui.components.NavigateBackButton
import uvis.irin.grape.soundlist.ui.model.DownloadState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SoundListTopAppBar(
    modifier: Modifier = Modifier,
    category: UiCategory,
    searchQuery: String,
    isSearchExpanded: Boolean,
    soundsLoaded: Boolean,
    soundsDownloadState: DownloadState,
    onNavigationIconClicked: () -> Unit,
    onDownloadForOfflineIconClicked: () -> Unit,
    onClearSearchQueryClicked: () -> Unit,
    onSearchToggleIconClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    Column {
        TopAppBar(
            modifier = modifier,
            title = {
                AnimatedContent(targetState = isSearchExpanded) { searchExpanded ->
                    if (!searchExpanded) {
                        AppBarTitle(text = category.name)
                    } else {
                        AppBarTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChanged,
                            hint = stringResource(R.string.searchSoundsHint),
                        )
                    }
                }
            },
            navigationIcon = {
                NavigateBackButton(
                    onClick = onNavigationIconClicked,
                    contentDescription = stringResource(R.string.navigationIconContentDescription),
                )
            },
            actions = {
//                AnimatedVisibility(visible = soundsLoaded) {
//                    AnimatedVisibility(
//                        visible = !isSearchExpanded,
//                        exit = ExitTransition.None
//                    ) {
//                        DownloadButton(
//                            downloadState = soundsDownloadState,
//                            onClick = onDownloadForOfflineIconClicked,
//                            contentDescription = stringResource(R.string.downloadCategorySoundsButtonContentDescription),
//                        )
//                    }
//                }

                AnimatedVisibility(visible = soundsLoaded) {
                    if (!isSearchExpanded) {
                        DownloadButton(
                            downloadState = soundsDownloadState,
                            onClick = onDownloadForOfflineIconClicked,
                            contentDescription = stringResource(R.string.downloadCategorySoundsButtonContentDescription),
                        )
                    } else {
                        ClearTextButton(
                            onClick = onClearSearchQueryClicked,
                            contentDescription = stringResource(R.string.clearTextButtonContentDescription)
                        )
                    }
                }

                if (isSearchExpanded) {
                    CloseSearchButton(
                        onClick = onSearchToggleIconClicked,
                        contentDescription = stringResource(R.string.closeSearchButtonContentDescription)
                    )
                } else {
                    SearchButton(
                        onClick = onSearchToggleIconClicked,
                        contentDescription = stringResource(R.string.searchButtonContentDescription),
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }
}
