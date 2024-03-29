package uvis.irin.grape.soundlist.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import uvis.irin.grape.core.constants.smallPadding
import uvis.irin.grape.soundlist.ui.model.UiSound

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SoundListLoadedContent(
    modifier: Modifier = Modifier,
    isSynchronizing: Boolean,
    sounds: List<UiSound>,
    scrollBehavior: TopAppBarScrollBehavior,
    onSoundButtonClicked: (UiSound) -> Unit,
    onDownloadSoundClicked: (UiSound) -> Unit,
    onFavouriteButtonClicked: (UiSound) -> Unit,
    onShareButtonClicked: (UiSound) -> Unit,
) {
    Column {
        AnimatedVisibility(visible = isSynchronizing) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyColumn(
            modifier = modifier
                .padding(
                    horizontal = smallPadding,
                    vertical = smallPadding,
                )
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            items(sounds, key = { it.fileName }) { sound ->
                Column(
                    modifier = Modifier.animateItemPlacement()
                ) {
                    SoundRow(
                        sound = sound,
                        onSoundButtonClicked = onSoundButtonClicked,
                        onDownloadSoundClicked = onDownloadSoundClicked,
                        onFavouriteButtonClicked = onFavouriteButtonClicked,
                        onShareButtonClicked = onShareButtonClicked,
                    )

                    Divider()
                }
            }
        }
    }
}
