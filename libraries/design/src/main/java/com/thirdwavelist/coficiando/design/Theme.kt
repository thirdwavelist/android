package com.thirdwavelist.coficiando.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CoficiandoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        CoficiandoDarkColorPalette
    } else {
        CoficiandoLightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = CoficiandoTypography,
            shapes = CoficiandoShapes,
            content = content
    )
}