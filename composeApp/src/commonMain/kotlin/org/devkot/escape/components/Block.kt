package org.devkot.escape.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.devkot.escape.icons.Bitcoin

@Composable
fun Block(modifier: Modifier = Modifier) =
    Icon(
        Bitcoin,
        null,
        modifier.fillMaxSize().padding(1.dp),
        tint = Color.Cyan
    )