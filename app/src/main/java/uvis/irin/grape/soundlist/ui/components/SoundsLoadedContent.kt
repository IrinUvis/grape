package uvis.irin.grape.soundlist.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import uvis.irin.grape.soundlist.ui.model.UiSound

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundsLoadedContent(
    modifier: Modifier = Modifier,
    sounds: List<UiSound>,
    scrollBehavior: TopAppBarScrollBehavior,
    onSoundButtonClicked: (UiSound) -> Unit,
    onFavouriteButtonClicked: (UiSound) -> Unit,
    onShareButtonClicked: (UiSound) -> Unit,
) {
    LazyColumn(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(sounds) { sound ->
            Column {
                SoundRow(
                    sound = sound,
                    onSoundButtonClicked = onSoundButtonClicked,
                    onFavouriteButtonClicked = onFavouriteButtonClicked,
                    onShareButtonClicked = onShareButtonClicked,
                )

                Divider()
            }
        }
    }
}
