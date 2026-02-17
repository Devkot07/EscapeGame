package org.devkot.escape

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.devkot.escape.screens.MenuScreen
import org.devkot.escape.screens.PlayingScreen
import org.devkot.escape.screens.WinScreen
import org.devkot.escape.viewmodels.GameViewModel
import org.devkot.escape.states.UiState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun App() {
    val viewModel: GameViewModel = viewModel { GameViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    val isAndroid = remember {
        System.getProperty("java.vendor")?.contains("Android", ignoreCase = true) == true
    }

    Box(Modifier.fillMaxSize()) {
        when (val state = uiState) {
            UiState.Menu -> MenuScreen(startGame = viewModel::startGame)

            is UiState.Playing -> PlayingScreen(
                state = state,
                isAndroid = isAndroid,
                viewModel = viewModel
            )

            is UiState.Win -> WinScreen(
                moneyCount = state.moneyCount,
                returnToMenu = viewModel::openMenu
            )
        }
    }
}


