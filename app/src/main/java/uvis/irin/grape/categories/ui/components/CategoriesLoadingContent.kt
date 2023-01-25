package uvis.irin.grape.categories.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uvis.irin.grape.R
import uvis.irin.grape.core.constants.mediumPadding
import uvis.irin.grape.core.ui.components.GrapeLoadingIndicator
import uvis.irin.grape.core.ui.components.VerticalSpacer

@Composable
fun CategoriesLoadingContent(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GrapeLoadingIndicator()

            VerticalSpacer(height = mediumPadding)

            Text(text = stringResource(R.string.loading))
        }
    }
}
