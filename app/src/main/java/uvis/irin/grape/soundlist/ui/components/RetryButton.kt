package uvis.irin.grape.soundlist.ui.components

import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import uvis.irin.grape.R

@Composable
fun RetryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentDescription: String,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.semantics {
                this.contentDescription = contentDescription
            },
            text = stringResource(R.string.retry),
        )
    }
}
