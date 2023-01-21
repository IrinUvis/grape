package uvis.irin.grape.soundlist.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uvis.irin.grape.R
import uvis.irin.grape.core.constants.mediumPadding
import uvis.irin.grape.core.ui.components.VerticalSpacer

private const val ANIMATION_DURATION = 1500
private const val MIN_ROTATION_ANGLE = 0f
private const val MAX_ROTATION_ANGLE = 360f
private val IMAGE_SIZE = 72.dp


@Composable
fun LoadingContent(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val infiniteTransition = rememberInfiniteTransition()
        val rotationAngle by infiniteTransition.animateFloat(
            initialValue = MIN_ROTATION_ANGLE,
            targetValue = MAX_ROTATION_ANGLE,
            animationSpec = infiniteRepeatable(
                animation = tween(ANIMATION_DURATION, easing = LinearEasing)
            )
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(IMAGE_SIZE)
                    .graphicsLayer {
                        rotationZ = rotationAngle
                    },
                painter = painterResource(R.drawable.smutny_2),
                contentDescription = null,
            )

            VerticalSpacer(height = mediumPadding)

            Text(text = stringResource(R.string.loading))
        }

    }
}
