package org.devkot.escape.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import org.devkot.escape.enums.Cell
import org.devkot.escape.enums.VisionType
import org.devkot.escape.utils.calculateVisibilityGradient

@Composable
fun RenderCell(
    unitSize: Dp,
    cell: Cell,
    floorColor: Color,
    doorOpened: Boolean,
    distance: Float,
    visionType: VisionType,
    isVisible: Boolean
) {


    val isWall = cell == Cell.Border || cell == Cell.Door || cell == Cell.HeroOnDoor

    val isEmpty = cell == Cell.Empty || cell == Cell.Hero || cell == Cell.Block || cell == Cell.Key ||
            cell == Cell.ElevatorUp || cell == Cell.HeroOnElevatorUp ||
            cell == Cell.ElevatorDown || cell == Cell.HeroOnElevatorDown


    val backgroundAlpha = when (visionType) {
        VisionType.Normal -> {
            when {
                !isVisible && isWall -> 0.3f
                !isVisible -> 0f
                isEmpty -> 0.05f
                isWall -> 1f
                else -> 0f
            }
        }

        VisionType.Ghostly -> {
            when {
                isWall -> calculateVisibilityGradient(
                    distance,
                    maxDistance = 8.0f,
                    startAlpha = 0.6f,
                    multiplier = 1f
                )
                !isVisible -> 0f
                isEmpty -> calculateVisibilityGradient(
                    distance,
                    maxDistance = 9.0f,
                    startAlpha = 0.05f,
                    multiplier = 1f
                )

                else -> calculateVisibilityGradient(
                    distance,
                    maxDistance = 3.0f,
                    startAlpha = 1f,
                    multiplier = 1f
                )
            }
        }

        VisionType.Hardcore -> {
            when {
                isWall -> calculateVisibilityGradient(
                    distance,
                    maxDistance = 4.0f,
                    startAlpha = 0.6f,
                    multiplier = 1f
                )
                !isVisible -> 0f
                isEmpty -> calculateVisibilityGradient(
                    distance,
                    maxDistance = 4.0f,
                    startAlpha = 0.05f,
                    multiplier = 1f
                )

                else -> calculateVisibilityGradient(
                    distance,
                    maxDistance = 3.0f,
                    startAlpha = 1f,
                    multiplier = 1f
                )
            }
        }

    }

    val contentAlpha = when {
        !isVisible -> 0f
        isEmpty -> when (visionType) {
            VisionType.Normal -> 1f
            VisionType.Ghostly -> calculateVisibilityGradient(
                distance,
                maxDistance = 3.0f,
                startAlpha = 1f,
                multiplier = 1f
            )

            VisionType.Hardcore -> calculateVisibilityGradient(
                distance,
                maxDistance = 2.0f,
                startAlpha = 1f,
                multiplier = 0.6f
            )

        }

        else -> 1f
    }

    val animatedBackgroundAlpha by animateFloatAsState(
        targetValue = backgroundAlpha,
        animationSpec = tween(150),
        label = "backgroundAlpha"
    )

    val animatedContentAlpha by animateFloatAsState(
        targetValue = contentAlpha,
        animationSpec = tween(150),
    )

    Box(
        Modifier.size(unitSize),
        contentAlignment = Alignment.Center
    ) {
        when (cell) {
            Cell.Border -> Box(Modifier.graphicsLayer {
                alpha = animatedBackgroundAlpha
            }) {
                Border(color = floorColor)
            }

            Cell.Hero -> {
                Hero()
            }

            Cell.Block, Cell.Key,
            Cell.ElevatorUp, Cell.HeroOnElevatorUp,
            Cell.ElevatorDown, Cell.HeroOnElevatorDown,
            Cell.Empty -> {
                AnimatedContent(
                    targetState = cell,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(200)) togetherWith
                                fadeOut(animationSpec = tween(200))
                    }) { targetCell ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = animatedBackgroundAlpha }
                                .background(Color.White)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = animatedContentAlpha }
                        ) {
                            when (targetCell) {

                                Cell.Block -> Block()
                                Cell.Key -> KeyIcon()
                                Cell.ElevatorUp ->
                                    Elevator(isUp = true)

                                Cell.ElevatorDown ->
                                    Elevator(isUp = false)

                                Cell.HeroOnElevatorUp ->
                                    HeroOn {
                                        Elevator(isUp = true)
                                    }

                                Cell.HeroOnElevatorDown ->
                                    HeroOn {
                                        Elevator(isUp = false)
                                    }

                                else -> {}
                            }
                        }
                    }
                }
            }

            Cell.Door, Cell.HeroOnDoor -> {
                Box(Modifier.graphicsLayer { alpha = animatedBackgroundAlpha }) {
                    Crossfade(
                        targetState = animatedBackgroundAlpha > 0.4f,
                        animationSpec = if (animatedBackgroundAlpha > 0.8f) {
                            tween(300)
                        } else {
                            tween(300, delayMillis = 300)
                        },
                        label = "doorVisibility"
                    ) { isVisible ->
                        if (isVisible) {
                            Door(isOpen = doorOpened)
                        } else {
                            Border(color = floorColor)
                        }
                    }
                }
            }
        }
    }
}