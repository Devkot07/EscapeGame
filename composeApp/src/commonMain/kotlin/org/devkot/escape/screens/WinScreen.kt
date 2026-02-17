package org.devkot.escape.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.devkot.escape.components.Hero
import org.devkot.escape.icons.Bitcoin

@Composable
fun WinScreen(
    moneyCount: Int,
    returnToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize().background(Color.Black)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ПОБЕДА",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 12.sp,
                    shadow = Shadow(
                        color = Color(0xFF00FFFF),
                        blurRadius = 20f
                    )
                ),
                color = Color(0xFF00FFFF)
            )

            Spacer(modifier = Modifier.height(48.dp))

            HorizontalDivider(
                modifier = Modifier.width(500.dp),
                color = Color(0xFFF7E600),
                thickness = 3.dp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Hero(modifier = Modifier.size(120.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Bitcoin,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Cyan
                        )
                        Text(
                            text = "× $moneyCount",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }

                    Text(
                        text = "Ты смог сбежать!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 4.sp
                        ),
                        color = Color(0xFF00FFFF)
                    )
                }
            }
            Spacer(modifier = Modifier.height(48.dp))

            HorizontalDivider(
                modifier = Modifier.width(500.dp),
                color = Color(0xFFF7E600),
                thickness = 3.dp
            )
            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(56.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF00FFFF),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                    .clickable { returnToMenu() }
                    .shadow(8.dp, RoundedCornerShape(4.dp), ambientColor = Color(0xFF00FFFF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "МЕНЮ",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp
                    ),
                    color = Color(0xFF00FFFF)
                )
            }
        }
    }
}
