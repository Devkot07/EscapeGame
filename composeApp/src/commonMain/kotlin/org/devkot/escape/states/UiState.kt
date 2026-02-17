package org.devkot.escape.states

import org.devkot.escape.enums.VisionType
import org.devkot.escape.models.Floor

sealed class UiState {
    object Menu : UiState()
    data class Playing(
        val visionType: VisionType,
        val floor: Floor,
        val keyCount: Int,
        val moneyCount: Int,
        val doorOpened: Boolean,
        val heroX: Int,
        val heroY: Int
    ) : UiState()

    data class Win(val moneyCount: Int) : UiState()
}