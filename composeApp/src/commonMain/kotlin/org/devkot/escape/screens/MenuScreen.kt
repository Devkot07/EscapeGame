package org.devkot.escape.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.devkot.escape.components.ChoosingLevelButton
import org.devkot.escape.enums.VisionType


@Composable
fun MenuScreen(
    startGame: (VisionType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ESCAPE",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 8.sp
            ),
            color = Color(0xFF00FFFF)
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
            modifier = Modifier.width(300.dp),
            color = Color(0xFFF7E600),
            thickness = 2.dp
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(
            modifier = Modifier.width(280.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ChoosingLevelButton("Легко", VisionType.Normal, color = Color(0xFF00FFFF), startGame)
            ChoosingLevelButton("Сложно", VisionType.Ghostly, color = Color(0xFF00FFFF), startGame)
            ChoosingLevelButton("Невозможно", VisionType.Hardcore, color = Color(0xFF00FFFF), startGame)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Выберите уровень",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Gray
        )
    }
}


