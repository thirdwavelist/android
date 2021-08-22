package com.thirdwavelist.coficiando.settings.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SettingsScreen(
    upPress: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Red) {
        SettingsContent()
    }
}

@Composable
fun SettingsContent() {

}
