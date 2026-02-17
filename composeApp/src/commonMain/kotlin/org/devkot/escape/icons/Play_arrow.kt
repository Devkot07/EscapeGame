package org.devkot.escape.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("ObjectPropertyName")
private var _Play: ImageVector? = null
val Play: ImageVector
    get() {
        if (_Play != null) return _Play!!
        _Play = ImageVector.Builder(
            name = "Play_arrow",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(320f, 760f)
                verticalLineToRelative(-560f)
                lineTo(760f, 480f)
                close()
            }
        }.build()
        return _Play!!
    }