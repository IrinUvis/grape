package uvis.irin.grape.soundlist.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uvis.irin.grape.R
import uvis.irin.grape.core.constants.extraSmallPadding
import uvis.irin.grape.soundlist.ui.model.UiSound


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun SoundRow(
    modifier: Modifier = Modifier,
    sound: UiSound,
    onSoundButtonClicked: (UiSound) -> Unit,
    onDownloadSoundClicked: (UiSound) -> Unit,
    onFavouriteButtonClicked: (UiSound) -> Unit,
    onShareButtonClicked: (UiSound) -> Unit,
) {
    Row(
        modifier = modifier.padding(vertical = extraSmallPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlaySoundButton(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = extraSmallPadding),
            soundName = sound.name,
            onCLick = { onSoundButtonClicked(sound) },
            contentDescription = stringResource(R.string.playSoundButtonContentDescription),
        )

        DownloadButton(
            downloadState = sound.downloadState,
            onClick = { onDownloadSoundClicked(sound) },
            contentDescription = stringResource(R.string.shareSoundButtonContentDescription),
        )

        FavouriteToggleButton(
            isFavourite = sound.isFavourite,
            onCheckedChange = { onFavouriteButtonClicked(sound) },
            contentDescription = stringResource(R.string.favouriteSoundToggleButtonContentDescription),
        )

        ShareButton(
            onClick = { onShareButtonClicked(sound) },
            contentDescription = stringResource(R.string.shareSoundButtonContentDescription),
        )
    }
}
