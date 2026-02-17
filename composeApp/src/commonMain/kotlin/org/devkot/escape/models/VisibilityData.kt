package org.devkot.escape.models

data class VisibilityData(
    private val data: Array<Array<CellVisibility>>,
    val heroX: Int,
    val heroY: Int
) {
    fun get(x: Int, y: Int): CellVisibility? {
        return data.getOrNull(y)?.getOrNull(x)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VisibilityData

        if (heroX != other.heroX) return false
        if (heroY != other.heroY) return false
        if (!data.contentDeepEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = heroX
        result = 31 * result + heroY
        result = 31 * result + data.contentDeepHashCode()
        return result
    }
}