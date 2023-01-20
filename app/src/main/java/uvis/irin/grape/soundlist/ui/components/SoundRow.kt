package uvis.irin.grape.soundlist.ui.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
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
import uvis.irin.grape.core.constants.extraSmallPadding
import uvis.irin.grape.core.ui.components.MarqueeText
import uvis.irin.grape.soundlist.ui.model.UiSound

private const val FAVOURITE_ANIMATION_DURATION = 250
private const val FAVOURITE_KEYFRAME_MILLIS_1 = 0
private const val FAVOURITE_KEYFRAME_MILLIS_2 = 15
private const val FAVOURITE_KEYFRAME_MILLIS_3 = 75
private const val FAVOURITE_KEYFRAME_MILLIS_4 = 150
private val FAVOURITE_KEYFRAME_SIZE_1 = 24.dp
private val FAVOURITE_KEYFRAME_SIZE_2 = 26.dp
private val FAVOURITE_KEYFRAME_SIZE_3 = 30.dp
private val FAVOURITE_KEYFRAME_SIZE_4 = 28.dp

private val FAVOURITE_ICON_SIZE_TOGGLED_OFF = 24.dp
private val FAVOURITE_ICON_SIZE_TOGGLED_ON = 25.dp


@Composable
fun SoundRow(
    modifier: Modifier = Modifier,
    sound: UiSound,
    onSoundButtonClicked: (UiSound) -> Unit,
    onFavouriteButtonClicked: (UiSound) -> Unit,
    onShareButtonClicked: (UiSound) -> Unit,
) {
    Row(
        modifier = modifier.padding(vertical = extraSmallPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilledTonalButton(
            modifier = modifier
                .weight(1f)
                .padding(horizontal = extraSmallPadding),
            onClick = { onSoundButtonClicked(sound) },
        ) {
            MarqueeText(
                text = sound.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
            )
        }

        IconToggleButton(
            checked = sound.isFavourite,
            onCheckedChange = { onFavouriteButtonClicked(sound) },
        ) {
            val size by animateDpAsState(
                targetValue = if (sound.isFavourite) {
                    FAVOURITE_ICON_SIZE_TOGGLED_ON
                } else {
                    FAVOURITE_ICON_SIZE_TOGGLED_OFF
                },
                animationSpec = keyframes {
                    durationMillis = FAVOURITE_ANIMATION_DURATION

                    FAVOURITE_KEYFRAME_SIZE_1 at FAVOURITE_KEYFRAME_MILLIS_1 with LinearOutSlowInEasing
                    FAVOURITE_KEYFRAME_SIZE_2 at FAVOURITE_KEYFRAME_MILLIS_2 with FastOutLinearInEasing
                    FAVOURITE_KEYFRAME_SIZE_3 at FAVOURITE_KEYFRAME_MILLIS_3
                    FAVOURITE_KEYFRAME_SIZE_4 at FAVOURITE_KEYFRAME_MILLIS_4
                }
            )

            Icon(
                modifier = Modifier.size(size),
                imageVector = if (sound.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
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
