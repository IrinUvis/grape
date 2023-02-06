package uvis.irin.grape.soundlist.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import uvis.irin.grape.R

@Composable
fun CloseSearchButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentDescription: String,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_search_off_24),
            contentDescription = contentDescription,
        )
    }
}
