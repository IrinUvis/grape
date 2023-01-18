package uvis.irin.grape.soundlist.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uvis.irin.grape.R
import uvis.irin.grape.core.ui.components.MarqueeText
import uvis.irin.grape.soundlist.ui.model.UiSound

@Composable
fun SoundRow(
    modifier: Modifier = Modifier,
    sound: UiSound,
    onSoundButtonClicked: (UiSound) -> Unit,
    onFavouriteButtonClicked: (UiSound) -> Unit,
    onShareButtonClicked: (UiSound) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilledTonalButton(
            modifier = modifier
                .weight(1f)
                .padding(6.dp),
            onClick = { onSoundButtonClicked(sound) },
        ) {
            MarqueeText(
                text = sound.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
            )
        }

        IconToggleButton(
            checked = sound.favourite,
            onCheckedChange = { onFavouriteButtonClicked(sound) },
        ) {
            val size by animateDpAsState(
                targetValue = if (sound.favourite) 26.dp else 24.dp
            )

            Icon(
                modifier = Modifier.size(size),
                imageVector = if (sound.favourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(R.string.favouriteSoundToggleButtonContentDescription),
            )
        }

        IconButton(
            onClick = { onShareButtonClicked(sound) },
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = stringResource(R.string.shareSoundButtonContentDescription),
            )
        }
    }
}
