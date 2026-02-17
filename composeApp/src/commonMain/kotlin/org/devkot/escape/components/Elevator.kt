package org.devkot.escape.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Elevator(isUp: Boolean, modifier: Modifier = Modifier) {
    val neonColor = if (isUp) Color(0xFF00E1FF) else Color(0xFFFF0000)


    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .border(2.dp, SolidColor(neonColor), RoundedCornerShape(8.dp))
            .background(Color.Transparent),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
                    text = if (isUp) "▲" else "▼",
            color = neonColor,
            style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                shadow = Shadow(neonColor, blurRadius = 15f)
            )
        )
    }

}