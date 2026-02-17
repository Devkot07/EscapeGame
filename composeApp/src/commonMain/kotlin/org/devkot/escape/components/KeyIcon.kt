package org.devkot.escape.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.devkot.escape.icons.Key

@Composable
fun KeyIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Key,
        contentDescription = "Key",
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        tint = Color(0xFFFFD700)
    )
}