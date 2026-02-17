package org.devkot.escape.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import escape.composeapp.generated.resources.Res
import escape.composeapp.generated.resources.hero
import org.jetbrains.compose.resources.painterResource

@Composable
fun HeroOn(modifier: Modifier = Modifier, bg: @Composable () -> Unit) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
) {
    bg()
    Hero()
}

@Composable
fun Hero(modifier: Modifier = Modifier) = Box {
    Image(
        painter = painterResource(Res.drawable.hero),
        contentDescription = null,
        modifier = modifier
    )
}