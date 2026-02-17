package org.devkot.escape.models

import androidx.compose.ui.graphics.Color
import org.devkot.escape.enums.Cell

data class Floor(
    val number: Int,
    val color: Color,
    val roomName: String,
    val floorMap: Array<Array<Cell>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Floor

        if (number != other.number) return false
        if (color != other.color) return false
        if (roomName != other.roomName) return false
        if (!floorMap.contentDeepEquals(other.floorMap)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number
        result = 31 * result + color.hashCode()
        result = 31 * result + roomName.hashCode()
        result = 31 * result + floorMap.contentDeepHashCode()
        return result
    }
}