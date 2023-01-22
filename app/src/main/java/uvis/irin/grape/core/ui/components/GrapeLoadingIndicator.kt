package uvis.irin.grape.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uvis.irin.grape.R

private const val ANIMATION_DURATION = 1500
private const val MIN_ROTATION_ANGLE = 0f
private const val MAX_ROTATION_ANGLE = 360f
private val IMAGE_SIZE = 72.dp

@Composable
fun GrapeLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = MIN_ROTATION_ANGLE,
        targetValue = MAX_ROTATION_ANGLE,
        animationSpec = infiniteRepeatable(
            animation = tween(ANIMATION_DURATION, easing = LinearEasing)
        )
    )

    Image(
        modifier = modifier
            .size(IMAGE_SIZE)
            .graphicsLayer {
                rotationZ = rotationAngle
            },
        painter = painterResource(R.drawable.smutny_2),
        contentDescription = null,
    )
}
