package org.devkot.escape

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Escape",
        undecorated = true,
        state = rememberWindowState(
            placement = WindowPlacement.Fullscreen,
        )
    ) {
        App()

    }
}