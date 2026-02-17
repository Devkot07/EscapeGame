package org.devkot.escape.utils

import org.devkot.escape.enums.Cell
import org.devkot.escape.models.CellVisibility
import org.devkot.escape.models.VisibilityData
import kotlin.math.abs
import kotlin.math.sqrt

fun calculateVisibilityData(
    heroX: Int,
    heroY: Int,
    map: Array<Array<Cell>>,
    maxDistance: Float
): VisibilityData {
    val height = map.size
    val width = if (height > 0) map[0].size else 0

    val visibilityArray = Array(height) { y ->
        Array(width) { x ->
            val dist = calculateDistance(heroX, heroY, x, y)

            val isVisible = if (dist <= maxDistance) {
                checkLineOfSight(heroX, heroY, x, y, map)
            } else {
                false
            }

            CellVisibility(dist, isVisible)
        }
    }

    return VisibilityData(visibilityArray, heroX, heroY)
}

private fun calculateDistance(x1: Int, y1: Int, x2: Int, y2: Int): Float {
    val dx = (x2 - x1).toFloat()
    val dy = (y2 - y1).toFloat()
    return sqrt(dx * dx + dy * dy)
}

fun calculateVisibilityGradient(
    distance: Float,
    maxDistance: Float,
    startAlpha: Float = 1f,
    multiplier: Float = 1f
): Float {
    return when {
        distance <= 1f -> startAlpha * multiplier
        distance <= maxDistance -> {
            val progress = (distance - 1f) / (maxDistance - 1f)
            val fade = startAlpha * (1f - progress)
            (fade * multiplier).coerceAtLeast(0f)
        }

        else -> 0f
    }
}

private fun checkLineOfSight(
    heroX: Int,
    heroY: Int,
    targetX: Int,
    targetY: Int,
    map: Array<Array<Cell>>
): Boolean {
    if (heroX == targetX && heroY == targetY) return true

    val dx = abs(targetX - heroX)
    val dy = abs(targetY - heroY)

    val sx = if (heroX < targetX) 1 else -1
    val sy = if (heroY < targetY) 1 else -1

    var err = dx - dy
    var currentX = heroX
    var currentY = heroY

    while (true) {
        if (currentX == targetX && currentY == targetY) {
            return true
        }

        if (currentY in map.indices && currentX in map[currentY].indices) {
            val cell = map[currentY][currentX]

            if (cell == Cell.Border || cell == Cell.Door) {
                if (currentX == targetX && currentY == targetY) {
                    return true
                }
                return false
            }
        }

        val e2 = 2 * err

        if (e2 > -dy) {
            err -= dy
            currentX += sx
        }

        if (e2 < dx) {
            err += dx
            currentY += sy
        }
    }
}