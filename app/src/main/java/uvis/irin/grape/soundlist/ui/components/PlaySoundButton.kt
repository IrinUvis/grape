package uvis.irin.grape.soundlist.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaySoundButton(
    modifier: Modifier = Modifier,
    soundName: String,
    onCLick: () -> Unit,
    contentDescription: String,
) {
    FilledTonalButton(
        modifier = modifier,
        onClick = onCLick,
    ) {
        Text(
            modifier = Modifier
                .basicMarquee(iterations = Int.MAX_VALUE)
                .semantics {
                    this.contentDescription = contentDescription
                },
            text = soundName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
