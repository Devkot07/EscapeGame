package org.devkot.escape.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.devkot.escape.models.CellVisibility
import org.devkot.escape.models.VisibilityData
import org.devkot.escape.utils.calculateVisibilityData
import org.devkot.escape.states.UiState


@Composable
fun AnimatedFloorContainer(state: UiState.Playing, unitSize: Dp) {

    var visibilityData by remember {
        mutableStateOf<VisibilityData?>(null)
    }

    LaunchedEffect(state.heroX, state.heroY, state.floor.number) {
        visibilityData = withContext(Dispatchers.Default) {
            calculateVisibilityData(
                heroX = state.heroX,
                heroY = state.heroY,
                map = state.floor.floorMap,
                maxDistance = 15f
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val map = state.floor.floorMap
        val visibility = visibilityData

        Box(modifier = Modifier.wrapContentSize()) {
            Column {
                repeat(map.size) { y ->
                    Row {
                        val row = map[y]
                        repeat(row.size) { x ->
                            val cell = row[x]

                            val cellVisibility = visibility?.get(x, y)
                                ?: CellVisibility(1000f, false)

                            key(state.floor.number, y, x) {
                                RenderCell(
                                    unitSize = unitSize,
                                    cell = cell,
                                    floorColor = state.floor.color,
                                    doorOpened = state.doorOpened,
                                    distance = cellVisibility.distance,
                                    visionType = state.visionType,
                                    isVisible = cellVisibility.isVisible
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


