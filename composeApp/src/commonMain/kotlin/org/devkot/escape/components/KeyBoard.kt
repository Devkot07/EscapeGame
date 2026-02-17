package org.devkot.escape.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.key.*

@Composable
fun KeyBoard(
    modifier: Modifier = Modifier,
    onKeyPress: (Key) -> Unit = {},
    onKeyRelease: (Key) -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            GameButton(
                text = "↑",
                onPress = { onKeyPress(Key.DirectionUp) },
                onRelease = { onKeyRelease(Key.DirectionUp) }
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.Center
            ) {

                GameButton(
                    text = "←",
                    onPress = { onKeyPress(Key.DirectionLeft) },
                    onRelease = { onKeyRelease(Key.DirectionLeft) }
                )

                GameButton(
                    text = "↓",
                    onPress = { onKeyPress(Key.DirectionDown) },
                    onRelease = { onKeyRelease(Key.DirectionDown) }
                )

                GameButton(
                    text = "→",
                    onPress = { onKeyPress(Key.DirectionRight) },
                    onRelease = { onKeyRelease(Key.DirectionRight) }
                )
            }
        }
    }
}


@Composable
fun GameButton(
    text: String,
    onPress: () -> Unit,
    onRelease: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val glowColor = Color(0xFF00FFFF)
    val borderAlpha by animateFloatAsState(if (isPressed) 1f else 0.5f)
    val scale by animateFloatAsState(if (isPressed) 0.92f else 1f)

    Box(
        modifier = Modifier
            .size(64.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Press -> {
                                isPressed = true
                                onPress()
                            }

                            PointerEventType.Release,
                            PointerEventType.Exit -> {
                                if (isPressed) {
                                    isPressed = false
                                    onRelease()
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = glowColor.copy(alpha = borderAlpha),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = glowColor.copy(alpha = if (isPressed) 1f else 0.7f),
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp
        )
    }
}
