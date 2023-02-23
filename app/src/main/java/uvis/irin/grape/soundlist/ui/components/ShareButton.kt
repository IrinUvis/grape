package uvis.irin.grape.soundlist.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShareButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentDescription: String,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = contentDescription,
        )
    }
}
