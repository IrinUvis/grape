package uvis.irin.grape.soundlist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import uvis.irin.grape.core.constants.mediumPadding
import uvis.irin.grape.core.ui.components.VerticalSpacer
import uvis.irin.grape.core.ui.helpers.errorImagePainterResources

@Composable
fun LoadingErrorContent(
    modifier: Modifier = Modifier,
    errorMessage: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(errorImagePainterResources.random()),
            contentDescription = null,
        )

        VerticalSpacer(height = mediumPadding)

        Text(
            text = errorMessage,
            textAlign = TextAlign.Center,
        )
    }
}
