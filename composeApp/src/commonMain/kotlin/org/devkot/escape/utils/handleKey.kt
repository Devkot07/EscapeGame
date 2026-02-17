package org.devkot.escape.utils

import androidx.compose.ui.input.key.Key
import org.devkot.escape.viewmodels.GameViewModel
import org.devkot.escape.enums.Direction

fun handleKey(viewModel: GameViewModel, key: Key) {
    when (key) {
        Key.Companion.K -> viewModel.inc()

        Key.Companion.W, Key.Companion.DirectionUp ->
            viewModel.moveHero(Direction.Up)

        Key.Companion.S, Key.Companion.DirectionDown ->
            viewModel.moveHero(Direction.Down)

        Key.Companion.A, Key.Companion.DirectionLeft ->
            viewModel.moveHero(Direction.Left)

        Key.Companion.D, Key.Companion.DirectionRight ->
            viewModel.moveHero(Direction.Right)

        else -> {}
    }
}