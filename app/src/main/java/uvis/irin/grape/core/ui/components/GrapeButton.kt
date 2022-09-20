package uvis.irin.grape.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

//@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
//@Composable
//fun GrapeButton(
//    onClick: () -> Unit,
//    onLongClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
//    elevation: CardElevation? = CardDefaults.cardElevation(),
//    shape: Shape = Shapes.Full,
//    border: BorderStroke? = null,
//    colors: CardColors = CardDefaults.cardColors(),
//    content: @Composable RowScope.() -> Unit
//) {
//    val containerColor = colors.containerColor(enabled).value
//    val contentColor = colors.contentColor(enabled).value
//    val shadowElevation = elevation?.shadowElevation(enabled, interactionSource)?.value ?: 0.dp
//    val tonalElevation = elevation?.tonalElevation(enabled, interactionSource)?.value ?: 0.dp
//
//    Surface(
//        modifier = modifier
//            .padding(vertical = 4.dp)
//            .combinedClickable(
//                onClick = onClick,
//                onLongClick = onLongClick,
//            ),
//        shape = shape,
//        color = containerColor,
//        contentColor = contentColor,
//        tonalElevation = tonalElevation,
//        shadowElevation = shadowElevation,
//        border = border,
//    ) {
//        CompositionLocalProvider(LocalContentColor provides contentColor) {
//            ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
//                Row(
//                    Modifier
//                        .defaultMinSize(
//                            minWidth = ButtonDefaults.MinWidth,
//                            minHeight = ButtonDefaults.MinHeight
//                        )
//                        .combinedClickable(
//                            onClick = onClick,
//                            onLongClick = onLongClick,
//                        ),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically,
//                    content = content
//                )
//            }
//        }
//    }
//}
