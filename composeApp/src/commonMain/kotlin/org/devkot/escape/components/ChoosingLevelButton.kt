package org.devkot.escape.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.devkot.escape.enums.VisionType

@Composable
fun ChoosingLevelButton(
    label: String,
    type: VisionType,
    color: Color,
    onClick: (VisionType) -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }

    val animatedColor by animateColorAsState(if (isHovered) color else color.copy(alpha = 0.5f))
    val scale by animateFloatAsState(if (isHovered) 1.05f else 1f)

    OutlinedButton(
        onClick = { onClick(type) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, animatedColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isHovered) color.copy(alpha = 0.1f) else Color.Transparent,
            contentColor = animatedColor
        ),
        interactionSource = remember { MutableInteractionSource() }.also { source ->
            LaunchedEffect(source) {
                source.interactions.collect { interaction ->
                    when (interaction) {
                        is HoverInteraction.Enter -> isHovered = true
                        is HoverInteraction.Exit -> isHovered = false
                    }
                }
            }
        }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge
        )
    }
}