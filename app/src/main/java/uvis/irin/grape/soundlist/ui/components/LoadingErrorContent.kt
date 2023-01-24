package uvis.irin.grape.soundlist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import uvis.irin.grape.R
import uvis.irin.grape.core.constants.mediumPadding
import uvis.irin.grape.core.ui.components.VerticalSpacer
import uvis.irin.grape.core.ui.helpers.errorImagePainterResources

@Composable
fun LoadingErrorContent(
    modifier: Modifier = Modifier,
    onRetryButtonClicked: () -> Unit,
    errorMessage: String,
) {
    val errorImage by remember {
        mutableStateOf(errorImagePainterResources.random())
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(errorImage),
            contentDescription = null,
        )

        VerticalSpacer(height = mediumPadding)

        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )

        VerticalSpacer(height = mediumPadding)

        RetryButton(
            onClick = onRetryButtonClicked,
            contentDescription = stringResource(R.string.retryButtonContentDescription),
        )
    }
}
