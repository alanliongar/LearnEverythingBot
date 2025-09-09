// StatusBars.kt
package com.example.learneverythingbot.presentation.screen.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun ApplySystemBarsAppearance() {
    val view = LocalView.current
    if (view.isInEditMode) return

    val dark = isSystemInDarkTheme()

    SideEffect {
        val window = (view.context as Activity).window
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        // true = ícones escuros; false = ícones claros
        controller.isAppearanceLightStatusBars = !dark
        controller.isAppearanceLightNavigationBars = !dark
    }
}
