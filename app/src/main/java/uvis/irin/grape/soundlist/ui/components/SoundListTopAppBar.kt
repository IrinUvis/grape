package uvis.irin.grape.soundlist.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import uvis.irin.grape.R
import uvis.irin.grape.soundlist.ui.model.UiCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundListTopAppBar(
    modifier: Modifier = Modifier,
    category: UiCategory,
    onNavigationIconPressed: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    MediumTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = category.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconPressed,
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.navigationIconContentDescription),
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
