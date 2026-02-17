package org.devkot.escape.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.devkot.escape.components.AnimatedFloorContainer
import org.devkot.escape.components.InventoryKey
import org.devkot.escape.components.KeyBoard
import org.devkot.escape.icons.Back
import org.devkot.escape.icons.Bitcoin
import org.devkot.escape.icons.Pause
import org.devkot.escape.icons.Play
import org.devkot.escape.utils.handleKey
import org.devkot.escape.viewmodels.GameViewModel
import org.devkot.escape.states.UiState


private const val INITIAL_DELAY = 200L
private const val REPEAT_DELAY = 100L


@Composable
fun PlayingScreen(
    state: UiState.Playing,
    isAndroid: Boolean,
    viewModel: GameViewModel
) {


    val focusRequester = remember { FocusRequester() }
    var pressedKey by remember { mutableStateOf<Key?>(null) }

    val pauseState = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = pauseState.value,
            transitionSpec = {
                fadeIn(animationSpec = tween(300))
                    .togetherWith(fadeOut(animationSpec = tween(300)))
                    .using(SizeTransform(clip = false))
            },
            modifier = Modifier.fillMaxSize(),
            label = "PauseTransition"
        ) { isPaused ->
            if (!isPaused) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = viewModel::openMenu) {
                            Icon(Back, contentDescription = null, tint = Color.White)
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.floor.roomName,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                fontSize = 18.sp,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Bitcoin,
                                    contentDescription = null,
                                    tint = Color(0xFF00FFFF),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${state.moneyCount}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00FFFF),
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))


                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(4) { index ->
                                    key(index) {
                                        InventoryKey(isCollected = index < state.keyCount)
                                    }
                                }
                            }
                        }


                        IconButton(onClick = { pauseState.value = true }) {
                            Icon(Pause, contentDescription = null, tint = Color.White)
                        }
                    }


                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }

                    LaunchedEffect(pressedKey) {
                        val currentKey = pressedKey ?: return@LaunchedEffect

                        handleKey(viewModel, currentKey)
                        delay(INITIAL_DELAY)
                        while (pressedKey == currentKey) {
                            handleKey(viewModel, currentKey)
                            delay(REPEAT_DELAY)
                        }
                    }

                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .focusRequester(focusRequester)
                            .focusable()
                            .onKeyEvent { event ->
                                when (event.type) {
                                    KeyEventType.KeyDown -> {
                                        if (pressedKey == null) pressedKey = event.key; true
                                    }

                                    KeyEventType.KeyUp -> {
                                        if (pressedKey == event.key) pressedKey = null; true
                                    }

                                    else -> false
                                }
                            }
                    ) {

                        val w = maxWidth
                        val h = maxHeight
                        val isLandscape = maxWidth > maxHeight

                        if (isLandscape) {
                            Row(
                                Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                GameArea(state, Modifier.weight(3f))

                                if (isAndroid) {
                                    ControlPad(
                                        modifier = Modifier.fillMaxHeight().width(w / 4),
                                        onKey = { pressedKey = it }
                                    )
                                }
                            }
                        } else {
                            Column(Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center) {
                                GameArea(state, Modifier.weight(3f))

                                if (isAndroid) {
                                    ControlPad(
                                        modifier = Modifier.fillMaxWidth().height(h / 3),
                                        onKey = { pressedKey = it }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("PAUSE", color = Color.White, fontSize = 48.sp)
                        Spacer(Modifier.height(24.dp))
                        Row {
                            IconButton(onClick = viewModel::openMenu) {
                                Icon(
                                    Back,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            Spacer(Modifier.width(48.dp))
                            IconButton(onClick = {
                                pauseState.value = false
                                focusRequester.requestFocus()
                            }) {

                                Icon(
                                    Play,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameArea(state: UiState.Playing, modifier: Modifier) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize().padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        val unitSize = minOf(maxWidth, maxHeight) / 22
        AnimatedFloorContainer(state, unitSize)
    }
}

@Composable
private fun ControlPad(modifier: Modifier, onKey: (Key?) -> Unit) {
    Box(modifier,
        contentAlignment = Alignment.Center) {
        KeyBoard(
            modifier = Modifier.padding(20.dp),
            onKeyPress = { onKey(it) },
            onKeyRelease = { onKey(null) }
        )
    }
}